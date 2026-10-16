# FreeLLM Hub Android App

A native Android application built with Kotlin and Jetpack Compose that unlocks access to 34+ free LLM providers and hundreds of free model endpoints directly from mobile with zero subscription paywalls.

## User Review & Critical Decisions

> [!IMPORTANT]
> **Summary of Confirmed Architectural Decisions:**
> - **Direct Mobile-to-Provider Connectivity**: The app communicates directly from the Android device to each AI provider's official free-tier REST endpoint (e.g., Google Gemini, Groq, Mistral, Cerebras, OpenRouter, Cloudflare Workers AI) using user-supplied free API keys or keyless endpoints. No external proxy server is required.
> - **Dual Core Experience (Chat Playground + Provider Hub)**: The app features both an AI chat playground with quick model switching and a dedicated Provider Management Hub for entering keys, checking rate limits, and testing endpoint latency.
> - **Unlocked Catalog with Live Discovery**: All 34+ free providers and hundreds of models are unlocked immediately (bypassing commercial 30-day catalog holdbacks). The app supports local presets as well as live remote catalog updates.

---

## 1. Overview & Core Concept

**FreeLLM Hub** brings the power of the open-source FreeLLMAPI catalog directly into a sleek, handheld Android experience:
- **Direct Free-Tier Access**: Enables developers and AI enthusiasts to maximize free monthly token allowances across major providers (Google Gemini 15 RPM free tier, Groq blazing fast inference, Mistral, OpenRouter free models, Cohere, Cerebras, and zero-key endpoints like Pollinations).
- **Zero Paywalls**: Bypasses catalog locks and tier restrictions so every available free model family (Llama 3.3, Qwen 2.5, DeepSeek, Gemini 3.5, Mistral NeMo, Phi-4) is selectable right away.
- **Privacy & Security**: All API keys remain on the user's device, encrypted in Android EncryptedSharedPreferences/DataStore.

---

## 2. User Experience & Visual Design

### Key User Flows
1. **First-Launch / Zero State**:
   - Welcome onboarding displaying total available free providers (34+) and immediate access to zero-configuration free models.
   - Quick Setup banner to input free keys (with direct 1-tap links to official free developer consoles like Google AI Studio, Groq Console, and Mistral).
2. **AI Chat Playground**:
   - Top Bar with active Model & Provider Chip (tap to open instant Model Selector Bottom Sheet).
   - Message timeline with user bubbles, model markdown output, formatted code snippets with 1-tap copy, token counts, and response latency (ms).
   - Dynamic prompt input bar with temperature/system prompt adjustments.
3. **Provider & Key Management Hub**:
   - Provider list categorized by strengths: *Ultra-Fast* (Groq, Cerebras), *Deep Context & Multimodal* (Google Gemini), *Open Weights* (Mistral, OpenRouter, Cloudflare), *Zero-Key / Free* (Pollinations, FreeAI).
   - Provider Card showing connection health status, configured key status (masked), quota hints, and a "Ping Test" button to verify latency.
4. **Model Catalog & Discovery**:
   - Filterable catalog of all available models with search, modality filter (Text, Vision, Code), and parameter controls.
   - Sync button to fetch the latest model definitions.

### Visual Identity & Theme
- **Aesthetic Direction**: High-tech cyber-minimalism with deep OLED dark mode.
- **Color Palette**:
  - Background: Deep Obsidian (`#0B0F17`) and Dark Surface (`#141B26`)
  - Primary Accent: Electric Cyan (`#00E5FF`)
  - Secondary Accent: Neon Indigo (`#7C4DFF`)
  - Success / Live: Emerald Glow (`#00E676`)
  - Surface Borders: Subtle Glass Stroke (`#1E293B`)
- **Typography & Motion**: Clean Android typography with crisp hierarchy; fluid spring animations on model switching and message streaming.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Direct Client-Side REST Architecture vs. Local Node Runtime**
  - *Chosen Approach*: Direct Kotlin/OkHttp/Retrofit HTTP client connecting directly from Android to provider endpoints.
  - *Why*: Running Node.js/SQLite server instances directly inside an Android mobile client adds huge APK size and background battery drain. Direct REST calls are fast, lightweight, standard for Android, and resilient.
- **Decision 2: Keyless Zero-Setup Out of the Box**
  - *Chosen Approach*: Provide immediately usable zero-key models (such as Pollinations AI, free public endpoints) alongside easy setup for standard free-tier keys (Gemini, Groq, OpenRouter).
  - *Why*: Allows new users to test the app immediately on launch before entering any API keys.
- **Decision 3: Local Persistence for Chat History & Keys**
  - *Chosen Approach*: Android DataStore and local SQLite/Room for conversation storage and secure key storage.
  - *Why*: Keeps user conversations and keys completely private to their personal device.

---

## 4. Technical Architecture & Data Strategy

### System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                       FreeLLM Hub UI                        │
│  ┌───────────────────────┐       ┌───────────────────────┐  │
│  │     Chat Playground   │       │  Provider & Keys Hub  │  │
│  │ (Streaming, Markdown) │       │ (Quotas, Latency Ping)│  │
│  └───────────┬───────────┘       └───────────┬───────────┘  │
└──────────────┼───────────────────────────────┼──────────────┘
               │                               │
               ▼                               ▼
┌─────────────────────────────────────────────────────────────┐
│                    ViewModel & State Layer                  │
│       (ChatViewModel, ProviderViewModel, CatalogViewModel)   │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                     Repository & Engine                     │
│  ┌───────────────────────┐       ┌───────────────────────┐  │
│  │   ModelCatalogEngine  │       │   LLMExecutionRouter  │  │
│  │ (34+ Unlocked Tiers)  │       │ (Direct Multi-Adapter)│  │
│  └───────────────────────┘       └───────────┬───────────┘  │
│  ┌───────────────────────┐                   │              │
│  │ Secure Key Storage    │                   │              │
│  │ (Encrypted Prefs)     │                   │              │
│  └───────────────────────┘                   │              │
└──────────────────────────────────────────────┼──────────────┘
                                               │
                                               ▼
┌─────────────────────────────────────────────────────────────┐
│             Direct Provider Network Adapters                │
│  ├── Google Gemini REST Adapter (generateContent)           │
│  ├── OpenAI-Compatible Adapter (Groq, Cerebras, Mistral,    │
│  │   OpenRouter, Cloudflare, DeepSeek)                      │
│  └── Keyless / Free Adapter (Pollinations AI)               │
└─────────────────────────────────────────────────────────────┘
```

### Target Android Project Setup
- **Module**: Android Application (`app/build.gradle.kts`, `settings.gradle.kts`, `build.gradle.kts`)
- **Package**: `com.freellm.hub`
- **Dependencies**:
  - Jetpack Compose & Material 3
  - AndroidX Lifecycle & ViewModel
  - OkHttp 4.x & Kotlinx Serialization for fast, direct streaming & JSON parsing
  - AndroidX Security Crypto / DataStore for secure key persistence
- **Verification**: Verified via `compile_applet`.
