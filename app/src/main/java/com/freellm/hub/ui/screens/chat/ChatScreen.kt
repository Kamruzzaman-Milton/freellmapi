package com.freellm.hub.ui.screens.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freellm.hub.data.model.ChatMessage
import com.freellm.hub.data.model.MessageRole
import com.freellm.hub.ui.components.CodeBlockView
import com.freellm.hub.ui.components.LatencyPill
import com.freellm.hub.ui.components.ProviderBadge
import com.freellm.hub.ui.theme.BackgroundDark
import com.freellm.hub.ui.theme.CoralError
import com.freellm.hub.ui.theme.CyberCyan
import com.freellm.hub.ui.theme.CyberPurple
import com.freellm.hub.ui.theme.GlassBorder
import com.freellm.hub.ui.theme.NeonEmerald
import com.freellm.hub.ui.theme.SurfaceCard
import com.freellm.hub.ui.theme.SurfaceCardHover
import com.freellm.hub.ui.theme.SurfaceDark
import com.freellm.hub.ui.theme.TextMuted
import com.freellm.hub.ui.theme.TextPrimary
import com.freellm.hub.ui.theme.TextSecondary

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onNavigateToProviders: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.messages.collectAsState()
    val activeModel by viewModel.activeModel.collectAsState()
    val activeProvider by viewModel.activeProvider.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val inputText by viewModel.inputText.collectAsState()

    var showModelSheet by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark)
                    .border(androidx.compose.foundation.BorderStroke(1.dp, GlassBorder))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Active Model Button
                    Row(
                        modifier = Modifier
                            .testTag("active_model_chip")
                            .clip(RoundedCornerShape(20.dp))
                            .background(SurfaceCard)
                            .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .clickable { showModelSheet = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(NeonEmerald)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = activeModel.displayName,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${activeProvider.name} • Unlocked Free",
                                color = CyberCyan,
                                fontSize = 10.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = "Switch Model",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.clearChat() },
                            modifier = Modifier.testTag("clear_chat_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Clear Chat",
                                tint = TextSecondary
                            )
                        }
                    }
                }
            }
        },
        containerColor = BackgroundDark,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            // Message Timeline
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Quick Starters Chips
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        Text(
                            text = "QUICK PROMPTS",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item {
                                QuickPromptChip(
                                    label = "⚡ Test Speed & Latency",
                                    onClick = { viewModel.sendQuickPrompt("Generate a quick bulleted summary of why open-source LLMs are accelerating AI research.") }
                                )
                            }
                            item {
                                QuickPromptChip(
                                    label = "🧠 Solve Logic Puzzle",
                                    onClick = { viewModel.sendQuickPrompt("A farmer needs to cross a river with a fox, a goose, and a bag of grain. Explain the step-by-step solution.") }
                                )
                            }
                            item {
                                QuickPromptChip(
                                    label = "💻 Kotlin Code Snippet",
                                    onClick = { viewModel.sendQuickPrompt("Show me a clean Jetpack Compose animation snippet in Kotlin.") }
                                )
                            }
                        }
                    }
                }

                items(messages, key = { it.id }) { msg ->
                    MessageBubble(
                        message = msg,
                        onConfigureKey = onNavigateToProviders
                    )
                }

                if (isLoading) {
                    item {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(SurfaceCard)
                                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                color = CyberCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Streaming response from ${activeModel.displayName}...",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Input composer
            Surface(
                color = SurfaceDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { viewModel.onInputChanged(it) },
                        placeholder = {
                            Text(
                                "Ask ${activeModel.displayName}...",
                                color = TextMuted,
                                fontSize = 14.sp
                            )
                        },
                        maxLines = 4,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = SurfaceCard,
                            unfocusedContainerColor = SurfaceCard,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (inputText.isNotBlank() && !isLoading) CyberCyan else SurfaceCard)
                            .clickable(enabled = inputText.isNotBlank() && !isLoading) {
                                viewModel.sendMessage()
                            }
                            .testTag("send_message_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = if (inputText.isNotBlank() && !isLoading) BackgroundDark else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    if (showModelSheet) {
        ModelSelectorSheet(
            currentModelId = activeModel.id,
            onModelSelected = { viewModel.selectModel(it) },
            onDismiss = { showModelSheet = false }
        )
    }
}

@Composable
fun QuickPromptChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
        modifier = modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun MessageBubble(
    message: ChatMessage,
    onConfigureKey: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isUser = message.role == MessageRole.USER
    val isError = message.role == MessageRole.ERROR

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        // Message Container
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = when {
                isUser -> CyberPurple
                isError -> CoralError.copy(alpha = 0.15f)
                else -> SurfaceCard
            },
            border = if (isError) androidx.compose.foundation.BorderStroke(1.dp, CoralError.copy(alpha = 0.5f))
            else if (!isUser) androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            else null,
            modifier = Modifier
                .widthIn(max = 340.dp)
                .testTag("message_bubble_${message.id}")
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Header for assistant messages
                if (!isUser && !isError && message.modelId != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = message.modelId,
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        if (message.latencyMs != null) {
                            LatencyPill(latencyMs = message.latencyMs)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // Render content (supporting code blocks if present)
                RenderFormattedMessage(content = message.content)

                // Error action button
                if (isError && message.content.contains("API Key required", ignoreCase = true)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberCyan)
                            .clickable { onConfigureKey() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Configure Key in Providers",
                            color = BackgroundDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Copy button for assistant responses
        if (!isUser && !isError) {
            Row(
                modifier = Modifier
                    .clickable {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("response", message.content)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Response copied", Toast.LENGTH_SHORT).show()
                    }
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy Response",
                    tint = TextMuted,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Copy text",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun RenderFormattedMessage(content: String) {
    // If the text contains code fences (```), split and render CodeBlockView
    if (content.contains("```")) {
        val parts = content.split("```")
        parts.forEachIndexed { index, part ->
            if (index % 2 == 1) {
                // Code block part
                val lines = part.trim().lines()
                val firstLine = lines.firstOrNull() ?: ""
                val isLangTag = firstLine.length < 15 && !firstLine.contains(" ")
                val lang = if (isLangTag) firstLine else "code"
                val code = if (isLangTag) lines.drop(1).joinToString("\n") else part.trim()
                CodeBlockView(code = code, language = lang)
            } else {
                // Regular markdown/text part
                if (part.isNotBlank()) {
                    Text(
                        text = part.trim(),
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    } else {
        Text(
            text = content,
            color = TextPrimary,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}
