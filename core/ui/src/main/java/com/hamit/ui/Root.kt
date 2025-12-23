package com.hamit.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hamit.navigation.Destination
import com.hamit.ui.theme.AppColors

@Composable
fun Root() {
    val firstScreen = rememberScreen(Destination.SplashScreen)
    val systemUi = rememberSystemUiController()
    systemUi.setStatusBarColor(AppColors.BLACK_BG, false)
    Navigator(firstScreen)
}
