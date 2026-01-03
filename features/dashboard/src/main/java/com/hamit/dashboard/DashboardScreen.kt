package com.hamit.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.hamit.navigation.Destination
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.LocalAppColors

object DashboardScreen : Screen {

    @Composable
    override fun Content() {
        val mainScreen = rememberScreen(Destination.HomeScreen) as Tab
        val colors = LocalAppColors.current
        TabNavigator(tab = mainScreen) {
            Scaffold(
                modifier = Modifier.Companion
                    .background(colors.background)
                    .navigationBarsPadding(),
                content = {
                    Box(Modifier.Companion
                        .fillMaxSize()
                        .padding(it)) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    Column {
                        Spacer(
                            Modifier.Companion
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                        Row(
                            modifier = Modifier.Companion
                                .fillMaxWidth()
                                .background(colors.card)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Companion.CenterVertically
                        ) {
                            val likeScreen = rememberScreen(Destination.LikeScreen) as Tab

                        }
                    }
                }
            )
        }
    }

}