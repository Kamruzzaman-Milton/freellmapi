package com.freellm.hub.ui.screens.providers

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.freellm.hub.data.model.Provider
import com.freellm.hub.data.model.ProviderCategory
import com.freellm.hub.ui.theme.AmberAlert
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
fun ProvidersScreen(
    viewModel: ProvidersViewModel,
    modifier: Modifier = Modifier
) {
    val providerStates by viewModel.providerStates.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    var editingProvider by remember { mutableStateOf<Provider?>(null) }

    val categories = listOf("All", "Keyless Free", "Configured", "Ultra-Fast", "Reasoning", "Long Context")

    val filteredProviders = providerStates.filter { state ->
        when (selectedCategory) {
            "Keyless Free" -> state.isKeyless
            "Configured" -> state.isConfigured
            "Ultra-Fast" -> state.provider.category == ProviderCategory.SPEED
            "Reasoning" -> state.provider.category == ProviderCategory.REASONING
            "Long Context" -> state.provider.category == ProviderCategory.BIG_CONTEXT
            else -> true
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark)
                    .border(androidx.compose.foundation.BorderStroke(1.dp, GlassBorder))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Free Provider Hub",
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "All 34+ free providers unlocked with zero paywalls",
                            color = CyberCyan,
                            fontSize = 12.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeonEmerald.copy(alpha = 0.15f))
                            .border(1.dp, NeonEmerald.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${providerStates.count { it.isConfigured }} Ready",
                            color = NeonEmerald,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Filter chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setCategory(cat) },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                selectedLabelColor = CyberCyan,
                                containerColor = SurfaceCard,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                selectedBorderColor = CyberCyan,
                                borderColor = GlassBorder
                            )
                        )
                    }
                }
            }
        },
        containerColor = BackgroundDark,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Unlocked Banner
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceCardHover,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CyberCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LockOpen,
                                contentDescription = "Unlocked",
                                tint = CyberCyan,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Unlimited Free Provider Access",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Direct-to-provider connections with zero middleman or subscription fees.",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            items(filteredProviders, key = { it.provider.id }) { state ->
                ProviderCard(
                    state = state,
                    onConfigureClick = { editingProvider = state.provider },
                    onPingClick = { viewModel.testPing(state.provider.id) }
                )
            }
        }
    }

    // Configure Key Dialog
    if (editingProvider != null) {
        val current = editingProvider!!
        val existingKey = viewModel.keyManager.getKey(current.id)
        val existingUrl = viewModel.keyManager.getCustomBaseUrl(current.id)

        ConfigureKeyDialog(
            provider = current,
            initialKey = existingKey,
            initialUrl = existingUrl,
            onDismiss = { editingProvider = null },
            onSave = { key, url ->
                viewModel.saveKey(current.id, key, url)
                editingProvider = null
            }
        )
    }
}

@Composable
fun ProviderCard(
    state: ProviderUiState,
    onConfigureClick: () -> Unit,
    onPingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (state.isConfigured) NeonEmerald.copy(alpha = 0.25f) else GlassBorder
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("provider_card_${state.provider.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = state.provider.name,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CyberPurple.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = state.provider.category.label,
                            color = CyberPurple,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Status Indicator
                if (state.isKeyless) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeonEmerald.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("KEYLESS FREE", color = NeonEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                } else if (state.isConfigured) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Configured",
                            tint = NeonEmerald,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("ACTIVE", color = NeonEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Needs Key",
                            tint = AmberAlert,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("NEEDS KEY", color = AmberAlert, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Quota Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ElectricBolt,
                    contentDescription = "Quota",
                    tint = CyberCyan,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = state.provider.freeQuotaDescription,
                    color = CyberCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = state.provider.description,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            // Ping result or masked key preview
            if (state.pingResult != null || state.maskedKey.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0F1522))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.maskedKey.isNotBlank()) {
                        Text(
                            text = "Key: ${state.maskedKey}",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    } else {
                        Text(
                            text = "Public Free Endpoint",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    if (state.pingResult != null) {
                        Text(
                            text = "Ping: ${state.pingResult}",
                            color = if (state.pingSuccess == true) NeonEmerald else CoralError,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ping Button
                OutlinedButton(
                    onClick = onPingClick,
                    enabled = !state.isPinging,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("ping_button_${state.provider.id}")
                ) {
                    if (state.isPinging) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            color = CyberCyan,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Testing...", fontSize = 12.sp)
                    } else {
                        Icon(imageVector = Icons.Default.ElectricBolt, contentDescription = "Ping", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Test Ping", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Configure Key Button
                Button(
                    onClick = onConfigureClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.isConfigured) SurfaceCardHover else CyberCyan,
                        contentColor = if (state.isConfigured) TextPrimary else BackgroundDark
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("configure_button_${state.provider.id}")
                ) {
                    Icon(imageVector = Icons.Default.Key, contentDescription = "Key", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (state.isConfigured) "Edit Key" else "Set Free Key",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun ConfigureKeyDialog(
    provider: Provider,
    initialKey: String,
    initialUrl: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var keyInput by remember { mutableStateOf(initialKey) }
    var urlInput by remember { mutableStateOf(initialUrl) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        title = {
            Column {
                Text(
                    text = "Configure ${provider.name}",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = provider.freeQuotaDescription,
                    color = CyberCyan,
                    fontSize = 12.sp
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (provider.signupUrl.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceCard)
                            .clickable {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(provider.signupUrl))
                                context.startActivity(browserIntent)
                            }
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Get Free API Key",
                                color = NeonEmerald,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Open official developer console",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open Signup",
                            tint = NeonEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = keyInput,
                    onValueChange = { keyInput = it },
                    label = { Text("API Key") },
                    placeholder = { Text("Paste your free key here") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("api_key_dialog_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                OutlinedTextField(
                    value = urlInput,
                    onValueChange = { urlInput = it },
                    label = { Text("Base URL (Optional override)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(keyInput, urlInput) },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = BackgroundDark),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("save_key_button")
            ) {
                Text("Save", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}
