package ru.topbun.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.topbun.navigation.SharedScreen
import ru.topbun.ui.theme.AppColors

@Composable
fun Root() {
    val firstScreen = rememberScreen(SharedScreen.SplashScreen)
    val systemUi = rememberSystemUiController()
    systemUi.setStatusBarColor(AppColors.BLACK_BG, false)
    Navigator(firstScreen)
}
