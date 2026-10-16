package com.freellm.hub.ui.screens.providers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.freellm.hub.data.local.KeyManager
import com.freellm.hub.data.local.ModelCatalog
import com.freellm.hub.data.model.Provider
import com.freellm.hub.data.remote.LLMClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProviderUiState(
    val provider: Provider,
    val isKeyless: Boolean,
    val isConfigured: Boolean,
    val maskedKey: String,
    val customBaseUrl: String,
    val isPinging: Boolean = false,
    val pingResult: String? = null,
    val pingSuccess: Boolean? = null
)

class ProvidersViewModel(application: Application) : AndroidViewModel(application) {

    val keyManager = KeyManager(application)
    private val client = LLMClient(keyManager)

    private val _providerStates = MutableStateFlow<List<ProviderUiState>>(emptyList())
    val providerStates: StateFlow<List<ProviderUiState>> = _providerStates.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    init {
        refreshProviders()
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun refreshProviders() {
        val list = ModelCatalog.providers.map { provider ->
            val hasKey = keyManager.hasKey(provider.id)
            val rawKey = keyManager.getKey(provider.id)
            val masked = if (rawKey.length > 8) {
                "${rawKey.take(3)}••••${rawKey.takeLast(4)}"
            } else if (rawKey.isNotEmpty()) {
                "••••••••"
            } else {
                ""
            }
            val lastPing = keyManager.getLastPing(provider.id)
            val pingStr = if (lastPing > 0) "${lastPing}ms" else null

            ProviderUiState(
                provider = provider,
                isKeyless = provider.isKeyless,
                isConfigured = hasKey,
                maskedKey = masked,
                customBaseUrl = keyManager.getCustomBaseUrl(provider.id),
                pingResult = pingStr,
                pingSuccess = if (lastPing > 0) true else null
            )
        }
        _providerStates.value = list
    }

    fun saveKey(providerId: String, apiKey: String, customUrl: String?) {
        keyManager.setKey(providerId, apiKey)
        if (!customUrl.isNullOrBlank()) {
            keyManager.setCustomBaseUrl(providerId, customUrl)
        }
        refreshProviders()
    }

    fun testPing(providerId: String) {
        val current = _providerStates.value.toMutableList()
        val index = current.indexOfFirst { it.provider.id == providerId }
        if (index == -1) return

        current[index] = current[index].copy(isPinging = true, pingResult = null)
        _providerStates.value = current

        viewModelScope.launch {
            val (success, message) = client.pingProvider(providerId)
            val updated = _providerStates.value.toMutableList()
            val upIndex = updated.indexOfFirst { it.provider.id == providerId }
            if (upIndex != -1) {
                updated[upIndex] = updated[upIndex].copy(
                    isPinging = false,
                    pingSuccess = success,
                    pingResult = message
                )
                _providerStates.value = updated
            }
        }
    }
}
