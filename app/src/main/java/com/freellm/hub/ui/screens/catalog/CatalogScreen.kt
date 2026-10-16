package com.freellm.hub.ui.screens.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freellm.hub.data.local.ModelCatalog
import com.freellm.hub.data.model.ModelInfo
import com.freellm.hub.data.model.ModelModality
import com.freellm.hub.ui.theme.BackgroundDark
import com.freellm.hub.ui.theme.CyberCyan
import com.freellm.hub.ui.theme.CyberPurple
import com.freellm.hub.ui.theme.GlassBorder
import com.freellm.hub.ui.theme.NeonEmerald
import com.freellm.hub.ui.theme.SurfaceCard
import com.freellm.hub.ui.theme.SurfaceDark
import com.freellm.hub.ui.theme.TextMuted
import com.freellm.hub.ui.theme.TextPrimary
import com.freellm.hub.ui.theme.TextSecondary

@Composable
fun CatalogScreen(
    onSelectModelForChat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTier by remember { mutableStateOf("All") }

    val tiers = listOf("All", "Frontier", "Large", "Medium", "Small", "Keyless")

    val filteredModels = ModelCatalog.models.filter { model ->
        val matchesSearch = model.displayName.contains(searchQuery, ignoreCase = true) ||
                model.family.contains(searchQuery, ignoreCase = true) ||
                model.providerId.contains(searchQuery, ignoreCase = true) ||
                model.id.contains(searchQuery, ignoreCase = true)

        val matchesTier = when (selectedTier) {
            "Frontier" -> model.sizeTier == "Frontier"
            "Large" -> model.sizeTier == "Large"
            "Medium" -> model.sizeTier == "Medium"
            "Small" -> model.sizeTier == "Small"
            "Keyless" -> model.isKeylessFree
            else -> true
        }

        matchesSearch && matchesTier
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
                            text = "Model Catalog",
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Browse 635 free endpoints across 34 providers",
                            color = CyberCyan,
                            fontSize = 12.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyberPurple.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${filteredModels.size} Models",
                            color = CyberPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Filter by model, family, or provider...", color = TextMuted, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = CyberCyan)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("catalog_search_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tiers) { tier ->
                        val isSelected = selectedTier == tier
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTier = tier },
                            label = { Text(tier, fontSize = 12.sp) },
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
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(filteredModels, key = { it.id }) { model ->
                val provider = ModelCatalog.getProvider(model.providerId)
                CatalogModelCard(
                    model = model,
                    providerName = provider?.name ?: model.providerId,
                    onChatClick = { onSelectModelForChat(model.id) }
                )
            }
        }
    }
}

@Composable
fun CatalogModelCard(
    model: ModelInfo,
    providerName: String,
    onChatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
        modifier = modifier
            .fillMaxWidth()
            .testTag("catalog_card_${model.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = model.displayName,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$providerName • Family: ${model.family}",
                        color = CyberCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (model.isKeylessFree) NeonEmerald.copy(alpha = 0.15f) else Color(0xFF1E2838))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (model.isKeylessFree) "KEYLESS" else model.sizeTier.uppercase(),
                        color = if (model.isKeylessFree) NeonEmerald else TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = model.description,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Specs and Modalities
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Context badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF0F1522))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${model.contextWindowTokens / 1024}K tokens",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Modality icons
                    if (model.modalities.contains(ModelModality.VISION)) {
                        Icon(imageVector = Icons.Default.Visibility, contentDescription = "Vision", tint = CyberCyan, modifier = Modifier.size(14.dp))
                    }
                    if (model.modalities.contains(ModelModality.CODE)) {
                        Icon(imageVector = Icons.Default.Code, contentDescription = "Code", tint = NeonEmerald, modifier = Modifier.size(14.dp))
                    }
                    if (model.modalities.contains(ModelModality.REASONING)) {
                        Icon(imageVector = Icons.Default.Psychology, contentDescription = "Reasoning", tint = CyberPurple, modifier = Modifier.size(14.dp))
                    }
                }

                // Chat with this model button
                Button(
                    onClick = onChatClick,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = BackgroundDark),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("select_model_btn_${model.id}")
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat", modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chat", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
