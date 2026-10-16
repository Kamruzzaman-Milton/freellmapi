package com.freellm.hub.data.remote

import com.freellm.hub.data.local.KeyManager
import com.freellm.hub.data.local.ModelCatalog
import com.freellm.hub.data.model.ChatMessage
import com.freellm.hub.data.model.MessageRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

sealed class LLMResult {
    data class Success(val text: String, val latencyMs: Long) : LLMResult()
    data class Error(val message: String, val statusCode: Int? = null) : LLMResult()
}

class LLMClient(private val keyManager: KeyManager) {

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun sendMessage(
        providerId: String,
        modelId: String,
        history: List<ChatMessage>,
        userPrompt: String,
        systemPrompt: String = keyManager.getSystemPrompt(),
        temperature: Float = keyManager.getTemperature()
    ): LLMResult = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        try {
            when (providerId) {
                "pollinations" -> callPollinations(modelId, history, userPrompt, systemPrompt)
                "google" -> callGoogleGemini(modelId, history, userPrompt, systemPrompt, temperature)
                else -> callOpenAICompatible(providerId, modelId, history, userPrompt, systemPrompt, temperature)
            }
        } catch (e: Exception) {
            val elapsed = System.currentTimeMillis() - startTime
            LLMResult.Error("Network error (${elapsed}ms): ${e.localizedMessage ?: "Unknown connection failure"}")
        }
    }

    suspend fun pingProvider(providerId: String): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        val provider = ModelCatalog.getProvider(providerId) ?: return@withContext Pair(false, "Provider not found")
        val modelId = provider.defaultModelId
        val startTime = System.currentTimeMillis()

        if (!provider.isKeyless && !keyManager.hasKey(providerId)) {
            return@withContext Pair(false, "No API key configured")
        }

        val result = sendMessage(
            providerId = providerId,
            modelId = modelId,
            history = emptyList(),
            userPrompt = "Say 1 word: Ready",
            systemPrompt = "Respond briefly."
        )

        val latency = System.currentTimeMillis() - startTime
        when (result) {
            is LLMResult.Success -> {
                keyManager.setLastPing(providerId, latency)
                Pair(true, "${latency}ms")
            }
            is LLMResult.Error -> {
                Pair(false, result.message)
            }
        }
    }

    private fun callPollinations(
        modelId: String,
        history: List<ChatMessage>,
        userPrompt: String,
        systemPrompt: String
    ): LLMResult {
        val startTime = System.currentTimeMillis()
        val messagesArray = JSONArray()

        if (systemPrompt.isNotBlank()) {
            messagesArray.put(JSONObject().apply {
                put("role", "system")
                put("content", systemPrompt)
            })
        }

        // Add history up to last 6 messages
        history.takeLast(6).forEach { msg ->
            if (msg.role == MessageRole.USER || msg.role == MessageRole.ASSISTANT) {
                messagesArray.put(JSONObject().apply {
                    put("role", if (msg.role == MessageRole.USER) "user" else "assistant")
                    put("content", msg.content)
                })
            }
        }

        messagesArray.put(JSONObject().apply {
            put("role", "user")
            put("content", userPrompt)
        })

        val payload = JSONObject().apply {
            put("messages", messagesArray)
            put("model", modelId)
            put("jsonMode", false)
        }

        val request = Request.Builder()
            .url("https://text.pollinations.ai/")
            .post(payload.toString().toRequestBody(jsonMediaType))
            .header("User-Agent", "FreeLLMHub-Android/1.0")
            .build()

        val response = httpClient.newCall(request).execute()
        val latency = System.currentTimeMillis() - startTime

        if (!response.isSuccessful) {
            val errorBody = response.body?.string() ?: ""
            return LLMResult.Error("Pollinations error ${response.code}: $errorBody", response.code)
        }

        val responseText = response.body?.string() ?: ""
        return LLMResult.Success(responseText.trim(), latency)
    }

    private fun callGoogleGemini(
        modelId: String,
        history: List<ChatMessage>,
        userPrompt: String,
        systemPrompt: String,
        temperature: Float
    ): LLMResult {
        val apiKey = keyManager.getKey("google")
        if (apiKey.isBlank()) {
            return LLMResult.Error("Google Gemini API Key is required. Please add your free key in the Providers tab.")
        }

        val startTime = System.currentTimeMillis()
        val contentsArray = JSONArray()

        history.takeLast(6).forEach { msg ->
            if (msg.role == MessageRole.USER || msg.role == MessageRole.ASSISTANT) {
                contentsArray.put(JSONObject().apply {
                    put("role", if (msg.role == MessageRole.USER) "user" else "model")
                    put("parts", JSONArray().put(JSONObject().put("text", msg.content)))
                })
            }
        }

        contentsArray.put(JSONObject().apply {
            put("role", "user")
            put("parts", JSONArray().put(JSONObject().put("text", userPrompt)))
        })

        val payload = JSONObject().apply {
            put("contents", contentsArray)
            if (systemPrompt.isNotBlank()) {
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
                })
            }
            put("generationConfig", JSONObject().apply {
                put("temperature", temperature)
                put("maxOutputTokens", 2048)
            })
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelId:generateContent?key=$apiKey"
        val request = Request.Builder()
            .url(url)
            .post(payload.toString().toRequestBody(jsonMediaType))
            .build()

        val response = httpClient.newCall(request).execute()
        val latency = System.currentTimeMillis() - startTime

        val bodyString = response.body?.string() ?: ""
        if (!response.isSuccessful) {
            val errorMsg = try {
                val json = JSONObject(bodyString)
                json.optJSONObject("error")?.optString("message") ?: bodyString
            } catch (e: Exception) {
                bodyString
            }
            return LLMResult.Error("Gemini error ${response.code}: $errorMsg", response.code)
        }

        return try {
            val json = JSONObject(bodyString)
            val candidates = json.getJSONArray("candidates")
            val first = candidates.getJSONObject(0)
            val parts = first.getJSONObject("content").getJSONArray("parts")
            val text = parts.getJSONObject(0).getString("text")
            LLMResult.Success(text.trim(), latency)
        } catch (e: Exception) {
            LLMResult.Error("Failed to parse Gemini response: ${e.message}")
        }
    }

    private fun callOpenAICompatible(
        providerId: String,
        modelId: String,
        history: List<ChatMessage>,
        userPrompt: String,
        systemPrompt: String,
        temperature: Float
    ): LLMResult {
        val apiKey = keyManager.getKey(providerId)
        val provider = ModelCatalog.getProvider(providerId)
        val baseUrl = keyManager.getCustomBaseUrl(providerId).trimEnd('/')

        if (provider?.isKeyless == false && apiKey.isBlank()) {
            return LLMResult.Error("API Key required for ${provider.name}. Enter your free key in the Providers tab.")
        }

        val startTime = System.currentTimeMillis()
        val messagesArray = JSONArray()

        if (systemPrompt.isNotBlank()) {
            messagesArray.put(JSONObject().apply {
                put("role", "system")
                put("content", systemPrompt)
            })
        }

        history.takeLast(6).forEach { msg ->
            if (msg.role == MessageRole.USER || msg.role == MessageRole.ASSISTANT) {
                messagesArray.put(JSONObject().apply {
                    put("role", if (msg.role == MessageRole.USER) "user" else "assistant")
                    put("content", msg.content)
                })
            }
        }

        messagesArray.put(JSONObject().apply {
            put("role", "user")
            put("content", userPrompt)
        })

        val payload = JSONObject().apply {
            put("model", modelId)
            put("messages", messagesArray)
            put("temperature", temperature)
            put("max_tokens", 2048)
        }

        val endpointUrl = if (baseUrl.endsWith("/chat/completions")) baseUrl else "$baseUrl/chat/completions"

        val requestBuilder = Request.Builder()
            .url(endpointUrl)
            .post(payload.toString().toRequestBody(jsonMediaType))
            .header("Content-Type", "application/json")

        if (apiKey.isNotBlank()) {
            requestBuilder.header("Authorization", "Bearer $apiKey")
        }

        if (providerId == "openrouter") {
            requestBuilder.header("HTTP-Referer", "https://freellmapi.co")
            requestBuilder.header("X-Title", "FreeLLM Hub")
        }

        val response = httpClient.newCall(requestBuilder.build()).execute()
        val latency = System.currentTimeMillis() - startTime

        val bodyString = response.body?.string() ?: ""
        if (!response.isSuccessful) {
            val errorMsg = try {
                val json = JSONObject(bodyString)
                json.optJSONObject("error")?.optString("message") ?: bodyString
            } catch (e: Exception) {
                bodyString
            }
            return LLMResult.Error("${provider?.name ?: providerId} error ${response.code}: $errorMsg", response.code)
        }

        return try {
            val json = JSONObject(bodyString)
            val choices = json.getJSONArray("choices")
            val firstChoice = choices.getJSONObject(0)
            val message = firstChoice.getJSONObject("message")
            val text = message.getString("content")
            LLMResult.Success(text.trim(), latency)
        } catch (e: Exception) {
            LLMResult.Error("Failed to parse response: ${e.message}")
        }
    }
}
