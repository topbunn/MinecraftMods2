package com.hamit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hamit.navigation.Destination
import com.hamit.ui.theme.AppTheme
import com.hamit.ui.theme.LocalAppColors

@Composable
fun Root() {
    AppTheme{
        val colors = LocalAppColors.current
        val firstScreen = rememberScreen(Destination.DashboardScreen)
        val systemUi = rememberSystemUiController()
        systemUi.setStatusBarColor(Color.Transparent, !isSystemInDarkTheme())
        Box(
            Modifier.fillMaxSize()
                .background(colors.background)
                .systemBarsPadding()
        ){
            Navigator(firstScreen)
        }
    }
}
