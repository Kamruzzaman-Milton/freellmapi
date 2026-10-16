package com.freellm.hub.data.model

enum class ModelModality {
    TEXT,
    VISION,
    CODE,
    REASONING
}

data class ModelInfo(
    val id: String,
    val displayName: String,
    val providerId: String,
    val family: String,
    val contextWindowTokens: Int,
    val modalities: List<ModelModality> = listOf(ModelModality.TEXT),
    val sizeTier: String = "Medium", // Small, Medium, Large, Frontier
    val description: String,
    val isRecommended: Boolean = false,
    val isKeylessFree: Boolean = false
)
