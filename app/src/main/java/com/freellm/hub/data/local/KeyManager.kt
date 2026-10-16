package com.freellm.hub.data.local

import android.content.Context
import android.content.SharedPreferences

class KeyManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("freellm_prefs", Context.MODE_PRIVATE)

    fun getKey(providerId: String): String {
        return prefs.getString("key_$providerId", "") ?: ""
    }

    fun setKey(providerId: String, key: String) {
        prefs.edit().putString("key_$providerId", key.trim()).apply()
    }

    fun hasKey(providerId: String): Boolean {
        if (providerId == "pollinations" || providerId == "aihorde") return true
        return getKey(providerId).isNotBlank()
    }

    fun getCustomBaseUrl(providerId: String): String {
        val fallback = ModelCatalog.getProvider(providerId)?.defaultBaseUrl ?: ""
        return prefs.getString("url_$providerId", fallback) ?: fallback
    }

    fun setCustomBaseUrl(providerId: String, url: String) {
        prefs.edit().putString("url_$providerId", url.trim()).apply()
    }

    fun getActiveModelId(): String {
        return prefs.getString("active_model", "openai") ?: "openai"
    }

    fun setActiveModelId(modelId: String) {
        prefs.edit().putString("active_model", modelId).apply()
    }

    fun getActiveProviderId(): String {
        val model = ModelCatalog.getModel(getActiveModelId())
        return model?.providerId ?: prefs.getString("active_provider", "pollinations") ?: "pollinations"
    }

    fun setActiveProviderId(providerId: String) {
        prefs.edit().putString("active_provider", providerId).apply()
    }

    fun getSystemPrompt(): String {
        return prefs.getString(
            "system_prompt",
            "You are a helpful, brilliant AI assistant powered by free unlocked LLM models."
        ) ?: "You are a helpful, brilliant AI assistant powered by free unlocked LLM models."
    }

    fun setSystemPrompt(prompt: String) {
        prefs.edit().putString("system_prompt", prompt).apply()
    }

    fun getTemperature(): Float {
        return prefs.getFloat("temperature", 0.7f)
    }

    fun setTemperature(temp: Float) {
        prefs.edit().putFloat("temperature", temp).apply()
    }

    fun getLastPing(providerId: String): Long {
        return prefs.getLong("ping_$providerId", -1L)
    }

    fun setLastPing(providerId: String, latencyMs: Long) {
        prefs.edit().putLong("ping_$providerId", latencyMs).apply()
    }
}
