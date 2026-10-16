# FreeLLM Hub (Android)

<div align="center">

**Native Android Client for Unlocked Free LLM Providers**

*Direct Mobile Inference • 34+ Free Provider Tiers • 635 Model Endpoints • Zero Paywalls*

</div>

---

## Overview

**FreeLLM Hub** brings the expansive model catalog of FreeLLMAPI directly to Android via Kotlin and Jetpack Compose. All artificial monthly catalog restrictions, tier gates, and proxy subscriptions are eliminated, allowing you to connect directly from your mobile device to official free provider tiers.

### Key Features

- **Direct Mobile-to-Provider Connectivity**: Connect directly from Android to Google Gemini, Groq, Cerebras, Mistral, OpenRouter, GitHub Models, Cohere, Cloudflare, DeepSeek, and more.
- **Zero-Key Keyless AI**: Includes Pollinations AI and AI Horde for instant, out-of-the-box text generation without registering or entering any API keys.
- **AI Chat Playground**: Modern Material 3 dark-themed chat interface with streaming text, markdown formatting, syntax-highlighted code blocks with 1-tap copy, token counts, and round-trip response latency (ms).
- **Instant Model Switcher**: Bottom sheet modal allowing instant switching between frontier reasoning models (DeepSeek R1, Gemini 2.5 Pro, Llama 3.3 70B), high-speed models (Groq, Cerebras), and multimodal models.
- **Provider Hub & Latency Ping Tester**: Manage API keys locally on your device, view rate limits and quotas for each provider, and run live ping tests to measure real-world network latency.
- **Privacy & Security**: All credentials remain strictly on your Android device in encrypted storage.

---

## Build & Run

- **Build System**: Gradle 9.x with Kotlin DSL (`build.gradle.kts`)
- **Android Target**: Android SDK 36 (minSdk 24)
- **UI Framework**: Jetpack Compose & Material 3
- **Network Stack**: OkHttp 4.x with direct coroutine execution
