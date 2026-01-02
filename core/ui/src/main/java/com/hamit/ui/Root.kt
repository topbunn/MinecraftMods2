package com.hamit.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hamit.ui.theme.AppColors

import com.hamit.navigation.Destination

@Composable
fun Root() {
    val firstScreen = rememberScreen(Destination.LoaderScreen)
    val systemUi = rememberSystemUiController()
    systemUi.setStatusBarColor(AppColors.BLACK_BG, false)
    Navigator(firstScreen)
}
