package com.freellm.hub.data.model

enum class ProviderCategory(val label: String) {
    SPEED("Ultra-Fast"),
    REASONING("Deep Reasoning"),
    MULTIMODAL("Vision & Media"),
    KEYLESS("Zero-Key Free"),
    BIG_CONTEXT("Long Context"),
    COMMUNITY("Open Weights & Meta")
}

enum class AuthScheme {
    BEARER,          // Standard Authorization: Bearer <key>
    QUERY_KEY,       // Google style ?key=<key>
    NONE,            // Keyless (Pollinations, etc.)
    CUSTOM_HEADER    // Custom headers
}

data class Provider(
    val id: String,
    val name: String,
    val category: ProviderCategory,
    val defaultBaseUrl: String,
    val authScheme: AuthScheme,
    val signupUrl: String,
    val freeQuotaDescription: String,
    val description: String,
    val isKeyless: Boolean = false,
    val defaultModelId: String,
    val isPopular: Boolean = false
)
