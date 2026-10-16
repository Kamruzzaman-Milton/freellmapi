package com.freellm.hub.data.local

import com.freellm.hub.data.model.AuthScheme
import com.freellm.hub.data.model.ModelInfo
import com.freellm.hub.data.model.ModelModality
import com.freellm.hub.data.model.Provider
import com.freellm.hub.data.model.ProviderCategory

object ModelCatalog {

    val providers: List<Provider> = listOf(
        Provider(
            id = "pollinations",
            name = "Pollinations AI",
            category = ProviderCategory.KEYLESS,
            defaultBaseUrl = "https://text.pollinations.ai/",
            authScheme = AuthScheme.NONE,
            signupUrl = "https://pollinations.ai",
            freeQuotaDescription = "100% Free & Unlimited • Zero Key Needed",
            description = "Community-powered keyless AI inference supporting modern open models.",
            isKeyless = true,
            defaultModelId = "openai",
            isPopular = true
        ),
        Provider(
            id = "google",
            name = "Google Gemini",
            category = ProviderCategory.BIG_CONTEXT,
            defaultBaseUrl = "https://generativelanguage.googleapis.com/v1beta",
            authScheme = AuthScheme.QUERY_KEY,
            signupUrl = "https://aistudio.google.com/app/apikey",
            freeQuotaDescription = "15 RPM • 1,000,000 TPM • 1,500 RPD Free",
            description = "Google's flagship models with massive context windows up to 2M tokens and multimodal reasoning.",
            defaultModelId = "gemini-2.5-flash",
            isPopular = true
        ),
        Provider(
            id = "groq",
            name = "Groq",
            category = ProviderCategory.SPEED,
            defaultBaseUrl = "https://api.groq.com/openai/v1",
            authScheme = AuthScheme.BEARER,
            signupUrl = "https://console.groq.com/keys",
            freeQuotaDescription = "30 RPM • 14,400 RPD • Up to 500 tok/s Free",
            description = "LPU inference engine delivering lightning-fast token generation for Llama 3.3 and DeepSeek R1.",
            defaultModelId = "llama-3.3-70b-versatile",
            isPopular = true
        ),
        Provider(
            id = "cerebras",
            name = "Cerebras",
            category = ProviderCategory.SPEED,
            defaultBaseUrl = "https://api.cerebras.ai/v1",
            authScheme = AuthScheme.BEARER,
            signupUrl = "https://cloud.cerebras.ai",
            freeQuotaDescription = "30 RPM • 1,000,000 tokens/day Free",
            description = "Wafer-scale engine with world record inference speeds surpassing 2,000 tokens per second.",
            defaultModelId = "llama3.3-70b",
            isPopular = true
        ),
        Provider(
            id = "mistral",
            name = "Mistral AI",
            category = ProviderCategory.COMMUNITY,
            defaultBaseUrl = "https://api.mistral.ai/v1",
            authScheme = AuthScheme.BEARER,
            signupUrl = "https://console.mistral.ai",
            freeQuotaDescription = "1 RPS • Free Experimenter Tier",
            description = "Premier European AI lab known for high-efficiency Mistral Small, NeMo, and Codestral.",
            defaultModelId = "mistral-small-latest",
            isPopular = true
        ),
        Provider(
            id = "openrouter",
            name = "OpenRouter (Free Tiers)",
            category = ProviderCategory.COMMUNITY,
            defaultBaseUrl = "https://openrouter.ai/api/v1",
            authScheme = AuthScheme.BEARER,
            signupUrl = "https://openrouter.ai/keys",
            freeQuotaDescription = "Completely free on all ':free' model variants",
            description = "Unified routing gateway aggregating dozens of free open models with zero cost tags.",
            defaultModelId = "meta-llama/llama-3.3-70b-instruct:free",
            isPopular = true
        ),
        Provider(
            id = "github",
            name = "GitHub Models",
            category = ProviderCategory.REASONING,
            defaultBaseUrl = "https://models.github.ai/inference",
            authScheme = AuthScheme.BEARER,
            signupUrl = "https://github.com/settings/tokens",
            freeQuotaDescription = "Free rate limits included with any GitHub account",
            description = "Microsoft and GitHub playground hosting frontier models for developers.",
            defaultModelId = "gpt-4o-mini",
            isPopular = true
        ),
        Provider(
            id = "cohere",
            name = "Cohere",
            category = ProviderCategory.REASONING,
            defaultBaseUrl = "https://api.cohere.com/v2",
            authScheme = AuthScheme.BEARER,
            signupUrl = "https://dashboard.cohere.com/api-keys",
            freeQuotaDescription = "20 RPM Free Trial Tier",
            description = "Enterprise language models specialized in RAG, command execution, and search reranking.",
            defaultModelId = "command-r-08-2024",
            isPopular = false
        ),
        Provider(
            id = "cloudflare",
            name = "Cloudflare Workers AI",
            category = ProviderCategory.COMMUNITY,
            defaultBaseUrl = "https://api.cloudflare.com/client/v4/accounts",
            authScheme = AuthScheme.BEARER,
            signupUrl = "https://dash.cloudflare.com",
            freeQuotaDescription = "10,000 neurons / day free allocation",
            description = "Global edge network running decentralized open-source models with minimal latency.",
            defaultModelId = "@cf/meta/llama-3.3-70b-instruct",
            isPopular = false
        ),
        Provider(
            id = "deepseek",
            name = "DeepSeek",
            category = ProviderCategory.REASONING,
            defaultBaseUrl = "https://api.deepseek.com/v1",
            authScheme = AuthScheme.BEARER,
            signupUrl = "https://platform.deepseek.com",
            freeQuotaDescription = "Free developer trial credits on signup",
            description = "Breakthrough open-weight frontier reasoning with DeepSeek R1 and DeepSeek V3.",
            defaultModelId = "deepseek-chat",
            isPopular = true
        ),
        Provider(
            id = "zhipu",
            name = "Zhipu AI (GLM)",
            category = ProviderCategory.BIG_CONTEXT,
            defaultBaseUrl = "https://open.bigmodel.cn/api/paas/v4",
            authScheme = AuthScheme.BEARER,
            signupUrl = "https://bigmodel.cn",
            freeQuotaDescription = "GLM-4-Flash is free indefinitely",
            description = "Tsinghua University ecosystem delivering powerful bilingual GLM-4 reasoning and vision.",
            defaultModelId = "glm-4-flash",
            isPopular = false
        ),
        Provider(
            id = "huggingface",
            name = "Hugging Face Inference",
            category = ProviderCategory.COMMUNITY,
            defaultBaseUrl = "https://router.huggingface.co/openai/v1",
            authScheme = AuthScheme.BEARER,
            signupUrl = "https://huggingface.co/settings/tokens",
            freeQuotaDescription = "Free router credits renewed monthly",
            description = "Hub of open machine learning models powered by Hugging Face's global serverless fleet.",
            defaultModelId = "meta-llama/Llama-3.2-3B-Instruct",
            isPopular = false
        ),
        Provider(
            id = "aihorde",
            name = "AI Horde",
            category = ProviderCategory.KEYLESS,
            defaultBaseUrl = "https://aihorde.net/api/v2",
            authScheme = AuthScheme.NONE,
            signupUrl = "https://aihorde.net",
            freeQuotaDescription = "100% Free Volunteer GPU Grid",
            description = "Decentralized crowdsourced GPU computing network run by volunteers worldwide.",
            isKeyless = true,
            defaultModelId = "horde-default",
            isPopular = false
        ),
        Provider(
            id = "custom",
            name = "Custom / Self-Hosted",
            category = ProviderCategory.COMMUNITY,
            defaultBaseUrl = "http://localhost:11434/v1",
            authScheme = AuthScheme.BEARER,
            signupUrl = "",
            freeQuotaDescription = "Unlimited • Direct to your own server",
            description = "Connect directly to any self-hosted FreeLLMAPI instance, local Ollama, vLLM, or LM Studio.",
            defaultModelId = "custom-model",
            isPopular = true
        )
    )

    val models: List<ModelInfo> = listOf(
        // Pollinations (Keyless & instant)
        ModelInfo(
            id = "openai",
            displayName = "GPT-4o Mini (Pollinations)",
            providerId = "pollinations",
            family = "GPT-4o",
            contextWindowTokens = 128000,
            modalities = listOf(ModelModality.TEXT, ModelModality.REASONING),
            sizeTier = "Medium",
            description = "Fast, reliable general reasoning model via Pollinations free tier.",
            isRecommended = true,
            isKeylessFree = true
        ),
        ModelInfo(
            id = "mistral",
            displayName = "Mistral Small (Pollinations)",
            providerId = "pollinations",
            family = "Mistral",
            contextWindowTokens = 32768,
            modalities = listOf(ModelModality.TEXT, ModelModality.CODE),
            sizeTier = "Small",
            description = "Keyless Mistral model with crisp coding and writing performance.",
            isRecommended = false,
            isKeylessFree = true
        ),
        ModelInfo(
            id = "qwen",
            displayName = "Qwen 2.5 72B (Pollinations)",
            providerId = "pollinations",
            family = "Qwen",
            contextWindowTokens = 32768,
            modalities = listOf(ModelModality.TEXT, ModelModality.CODE, ModelModality.REASONING),
            sizeTier = "Large",
            description = "Leading open-weight model with exceptional coding and multilingual fluency.",
            isRecommended = true,
            isKeylessFree = true
        ),
        ModelInfo(
            id = "deepseek",
            displayName = "DeepSeek R1 (Pollinations)",
            providerId = "pollinations",
            family = "DeepSeek",
            contextWindowTokens = 64000,
            modalities = listOf(ModelModality.TEXT, ModelModality.REASONING),
            sizeTier = "Frontier",
            description = "State-of-the-art chain-of-thought reasoning without requiring any API key.",
            isRecommended = true,
            isKeylessFree = true
        ),

        // Google Gemini
        ModelInfo(
            id = "gemini-2.5-flash",
            displayName = "Gemini 2.5 Flash",
            providerId = "google",
            family = "Gemini",
            contextWindowTokens = 1048576,
            modalities = listOf(ModelModality.TEXT, ModelModality.VISION, ModelModality.CODE),
            sizeTier = "Frontier",
            description = "Google's fast, multimodal model with 1M token context on free tier.",
            isRecommended = true
        ),
        ModelInfo(
            id = "gemini-2.5-pro",
            displayName = "Gemini 2.5 Pro",
            providerId = "google",
            family = "Gemini",
            contextWindowTokens = 2097152,
            modalities = listOf(ModelModality.TEXT, ModelModality.VISION, ModelModality.REASONING),
            sizeTier = "Frontier",
            description = "Frontier intelligence with 2 million token context and advanced reasoning.",
            isRecommended = true
        ),
        ModelInfo(
            id = "gemini-3.5-flash",
            displayName = "Gemini 3.5 Flash",
            providerId = "google",
            family = "Gemini",
            contextWindowTokens = 1048576,
            modalities = listOf(ModelModality.TEXT, ModelModality.CODE),
            sizeTier = "Frontier",
            description = "Next-generation fast inference model for general text and conversational tasks."
        ),

        // Groq
        ModelInfo(
            id = "llama-3.3-70b-versatile",
            displayName = "Llama 3.3 70B (Groq)",
            providerId = "groq",
            family = "Llama",
            contextWindowTokens = 128000,
            modalities = listOf(ModelModality.TEXT, ModelModality.CODE, ModelModality.REASONING),
            sizeTier = "Large",
            description = "Meta's most capable open-weight 70B model running at ~300 tokens/sec.",
            isRecommended = true
        ),
        ModelInfo(
            id = "llama-3.1-8b-instant",
            displayName = "Llama 3.1 8B (Groq)",
            providerId = "groq",
            family = "Llama",
            contextWindowTokens = 128000,
            modalities = listOf(ModelModality.TEXT, ModelModality.CODE),
            sizeTier = "Small",
            description = "Ultra-fast response times (~750 tok/s), ideal for instant chat and coding.",
            isRecommended = false
        ),
        ModelInfo(
            id = "deepseek-r1-distill-llama-70b",
            displayName = "DeepSeek R1 Distill 70B (Groq)",
            providerId = "groq",
            family = "DeepSeek",
            contextWindowTokens = 128000,
            modalities = listOf(ModelModality.TEXT, ModelModality.REASONING),
            sizeTier = "Large",
            description = "DeepSeek R1 reasoning architecture distilled into Llama 70B on Groq LPUs.",
            isRecommended = true
        ),

        // Cerebras
        ModelInfo(
            id = "llama3.3-70b",
            displayName = "Llama 3.3 70B (Cerebras)",
            providerId = "cerebras",
            family = "Llama",
            contextWindowTokens = 8192,
            modalities = listOf(ModelModality.TEXT, ModelModality.CODE),
            sizeTier = "Large",
            description = "Incredible generation speeds over 2,000 tokens per second.",
            isRecommended = true
        ),
        ModelInfo(
            id = "llama3.1-8b",
            displayName = "Llama 3.1 8B (Cerebras)",
            providerId = "cerebras",
            family = "Llama",
            contextWindowTokens = 8192,
            modalities = listOf(ModelModality.TEXT),
            sizeTier = "Small",
            description = "Instantaneous sub-second replies for real-time applications."
        ),

        // Mistral
        ModelInfo(
            id = "mistral-small-latest",
            displayName = "Mistral Small Latest",
            providerId = "mistral",
            family = "Mistral",
            contextWindowTokens = 32768,
            modalities = listOf(ModelModality.TEXT, ModelModality.CODE),
            sizeTier = "Medium",
            description = "Efficient, cost-free enterprise model with superior reasoning & coding.",
            isRecommended = true
        ),
        ModelInfo(
            id = "open-mistral-nemo",
            displayName = "Mistral NeMo 12B",
            providerId = "mistral",
            family = "Mistral",
            contextWindowTokens = 128000,
            modalities = listOf(ModelModality.TEXT, ModelModality.CODE),
            sizeTier = "Small",
            description = "12B multilingual model developed in collaboration with NVIDIA."
        ),
        ModelInfo(
            id = "codestral-latest",
            displayName = "Codestral Latest",
            providerId = "mistral",
            family = "Mistral",
            contextWindowTokens = 32768,
            modalities = listOf(ModelModality.CODE, ModelModality.TEXT),
            sizeTier = "Medium",
            description = "Dedicated code synthesis model fluent in 80+ programming languages.",
            isRecommended = true
        ),

        // OpenRouter Free
        ModelInfo(
            id = "meta-llama/llama-3.3-70b-instruct:free",
            displayName = "Llama 3.3 70B (OpenRouter Free)",
            providerId = "openrouter",
            family = "Llama",
            contextWindowTokens = 131072,
            modalities = listOf(ModelModality.TEXT, ModelModality.CODE),
            sizeTier = "Large",
            description = "Permanently free 70B model routed via OpenRouter.",
            isRecommended = true
        ),
        ModelInfo(
            id = "deepseek/deepseek-r1:free",
            displayName = "DeepSeek R1 (OpenRouter Free)",
            providerId = "openrouter",
            family = "DeepSeek",
            contextWindowTokens = 64000,
            modalities = listOf(ModelModality.TEXT, ModelModality.REASONING),
            sizeTier = "Frontier",
            description = "Free frontier reasoning model with deep thought scratchpad.",
            isRecommended = true
        ),
        ModelInfo(
            id = "qwen/qwen-2.5-coder-32b-instruct:free",
            displayName = "Qwen 2.5 Coder 32B (OpenRouter Free)",
            providerId = "openrouter",
            family = "Qwen",
            contextWindowTokens = 32768,
            modalities = listOf(ModelModality.CODE, ModelModality.TEXT),
            sizeTier = "Medium",
            description = "Specialized 32B coding engine rivaling much larger proprietary models."
        ),

        // GitHub Models
        ModelInfo(
            id = "gpt-4o-mini",
            displayName = "GPT-4o Mini (GitHub Models)",
            providerId = "github",
            family = "GPT-4o",
            contextWindowTokens = 128000,
            modalities = listOf(ModelModality.TEXT, ModelModality.VISION, ModelModality.CODE),
            sizeTier = "Medium",
            description = "Free developer tier for OpenAI models directly in GitHub Models.",
            isRecommended = true
        ),
        ModelInfo(
            id = "Phi-3.5-mini-instruct",
            displayName = "Phi-3.5 Mini (GitHub Models)",
            providerId = "github",
            family = "Phi",
            contextWindowTokens = 128000,
            modalities = listOf(ModelModality.TEXT, ModelModality.REASONING),
            sizeTier = "Small",
            description = "High efficiency lightweight model trained on synthetic textbooks."
        ),

        // DeepSeek Direct
        ModelInfo(
            id = "deepseek-chat",
            displayName = "DeepSeek V3",
            providerId = "deepseek",
            family = "DeepSeek",
            contextWindowTokens = 64000,
            modalities = listOf(ModelModality.TEXT, ModelModality.CODE),
            sizeTier = "Frontier",
            description = "671B Mixture-of-Experts architecture delivering frontier output.",
            isRecommended = true
        ),

        // Zhipu
        ModelInfo(
            id = "glm-4-flash",
            displayName = "GLM-4 Flash (Free Forever)",
            providerId = "zhipu",
            family = "GLM",
            contextWindowTokens = 128000,
            modalities = listOf(ModelModality.TEXT, ModelModality.CODE),
            sizeTier = "Small",
            description = "High-speed bilingual model completely free on Zhipu platform."
        ),

        // Custom
        ModelInfo(
            id = "custom-model",
            displayName = "Custom Endpoint Model",
            providerId = "custom",
            family = "Custom",
            contextWindowTokens = 32768,
            modalities = listOf(ModelModality.TEXT),
            sizeTier = "Medium",
            description = "Connects to your custom OpenAI-compatible server or local runtime."
        )
    )

    fun getProvider(providerId: String): Provider? {
        return providers.firstOrNull { it.id == providerId }
    }

    fun getModel(modelId: String): ModelInfo? {
        return models.firstOrNull { it.id == modelId }
    }

    fun getModelsForProvider(providerId: String): List<ModelInfo> {
        return models.filter { it.providerId == providerId }
    }
}
