package com.freellm.hub.ui.screens.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.freellm.hub.data.local.KeyManager
import com.freellm.hub.data.local.ModelCatalog
import com.freellm.hub.data.model.ChatMessage
import com.freellm.hub.data.model.MessageRole
import com.freellm.hub.data.model.ModelInfo
import com.freellm.hub.data.model.Provider
import com.freellm.hub.data.remote.LLMClient
import com.freellm.hub.data.remote.LLMResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    val keyManager = KeyManager(application)
    private val client = LLMClient(keyManager)

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _activeModel = MutableStateFlow<ModelInfo>(
        ModelCatalog.getModel(keyManager.getActiveModelId()) ?: ModelCatalog.models.first()
    )
    val activeModel: StateFlow<ModelInfo> = _activeModel.asStateFlow()

    private val _activeProvider = MutableStateFlow<Provider>(
        ModelCatalog.getProvider(_activeModel.value.providerId) ?: ModelCatalog.providers.first()
    )
    val activeProvider: StateFlow<Provider> = _activeProvider.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    init {
        // Add initial greeting message
        _messages.value = listOf(
            ChatMessage(
                role = MessageRole.ASSISTANT,
                content = "👋 Welcome to **FreeLLM Hub**!\n\nAll 34+ free provider tiers and models are unlocked. Currently connected to **${_activeModel.value.displayName}** on **${_activeProvider.value.name}**.\n\nYou can start chatting immediately, or switch models at any time using the model picker at the top.",
                providerId = _activeProvider.value.id,
                modelId = _activeModel.value.id
            )
        )
    }

    fun onInputChanged(text: String) {
        _inputText.value = text
    }

    fun selectModel(modelId: String) {
        val model = ModelCatalog.getModel(modelId) ?: return
        val provider = ModelCatalog.getProvider(model.providerId) ?: return
        keyManager.setActiveModelId(modelId)
        keyManager.setActiveProviderId(provider.id)
        _activeModel.value = model
        _activeProvider.value = provider
    }

    fun sendMessage() {
        val prompt = _inputText.value.trim()
        if (prompt.isBlank() || _isLoading.value) return

        _inputText.value = ""
        val userMsg = ChatMessage(role = MessageRole.USER, content = prompt)
        val currentHistory = _messages.value
        _messages.value = currentHistory + userMsg
        _isLoading.value = true

        val providerId = _activeProvider.value.id
        val modelId = _activeModel.value.id

        viewModelScope.launch {
            val result = client.sendMessage(
                providerId = providerId,
                modelId = modelId,
                history = currentHistory,
                userPrompt = prompt
            )

            _isLoading.value = false
            when (result) {
                is LLMResult.Success -> {
                    _messages.value = _messages.value + ChatMessage(
                        role = MessageRole.ASSISTANT,
                        content = result.text,
                        providerId = providerId,
                        modelId = modelId,
                        latencyMs = result.latencyMs
                    )
                }
                is LLMResult.Error -> {
                    _messages.value = _messages.value + ChatMessage(
                        role = MessageRole.ERROR,
                        content = "⚠️ ${result.message}",
                        providerId = providerId,
                        modelId = modelId
                    )
                }
            }
        }
    }

    fun sendQuickPrompt(prompt: String) {
        _inputText.value = prompt
        sendMessage()
    }

    fun clearChat() {
        _messages.value = emptyList()
    }
}
