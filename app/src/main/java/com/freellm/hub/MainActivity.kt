package com.freellm.hub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freellm.hub.data.local.KeyManager
import com.freellm.hub.ui.screens.catalog.CatalogScreen
import com.freellm.hub.ui.screens.chat.ChatScreen
import com.freellm.hub.ui.screens.chat.ChatViewModel
import com.freellm.hub.ui.screens.providers.ProvidersScreen
import com.freellm.hub.ui.screens.providers.ProvidersViewModel
import com.freellm.hub.ui.screens.settings.SettingsScreen
import com.freellm.hub.ui.theme.BackgroundDark
import com.freellm.hub.ui.theme.CyberCyan
import com.freellm.hub.ui.theme.FreeLLMHubTheme
import com.freellm.hub.ui.theme.GlassBorder
import com.freellm.hub.ui.theme.SurfaceDark
import com.freellm.hub.ui.theme.TextMuted
import com.freellm.hub.ui.theme.TextPrimary

class MainActivity : ComponentActivity() {

    private val chatViewModel: ChatViewModel by viewModels()
    private val providersViewModel: ProvidersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FreeLLMHubTheme(darkTheme = true) {
                MainAppShell(
                    chatViewModel = chatViewModel,
                    providersViewModel = providersViewModel
                )
            }
        }
    }
}

@Composable
fun MainAppShell(
    chatViewModel: ChatViewModel,
    providersViewModel: ProvidersViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val keyManager = remember { chatViewModel.keyManager }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceDark,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .border(androidx.compose.foundation.BorderStroke(1.dp, GlassBorder))
                    .navigationBarsPadding()
                    .testTag("main_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = stringResource(R.string.tab_chat)
                        )
                    },
                    label = { Text(stringResource(R.string.tab_chat), fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BackgroundDark,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_tab_chat")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        providersViewModel.refreshProviders()
                        selectedTab = 1
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Dns,
                            contentDescription = stringResource(R.string.tab_providers)
                        )
                    },
                    label = { Text(stringResource(R.string.tab_providers), fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BackgroundDark,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_tab_providers")
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ListAlt,
                            contentDescription = stringResource(R.string.tab_catalog)
                        )
                    },
                    label = { Text(stringResource(R.string.tab_catalog), fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BackgroundDark,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_tab_catalog")
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = stringResource(R.string.tab_settings)
                        )
                    },
                    label = { Text(stringResource(R.string.tab_settings), fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BackgroundDark,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_tab_settings")
                )
            }
        },
        containerColor = BackgroundDark,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> ChatScreen(
                    viewModel = chatViewModel,
                    onNavigateToProviders = { selectedTab = 1 }
                )
                1 -> ProvidersScreen(
                    viewModel = providersViewModel
                )
                2 -> CatalogScreen(
                    onSelectModelForChat = { modelId ->
                        chatViewModel.selectModel(modelId)
                        selectedTab = 0
                    }
                )
                3 -> SettingsScreen(
                    keyManager = keyManager
                )
            }
        }
    }
}
