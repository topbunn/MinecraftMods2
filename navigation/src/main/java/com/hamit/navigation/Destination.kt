package com.hamit.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class Destination : ScreenProvider {
    object NavigationScreen : Destination()
    object SplashScreen : Destination()
    object MainScreen : Destination()
    object ManualScreen : Destination()
    object FeedbackScreen : Destination()
    object LikeScreen : Destination()
    data class AddonScreen(val addonId: Int) : Destination()
}