package com.hamit.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class Destination : ScreenProvider {
    object DashboardScreen : Destination()
    object LoaderScreen : Destination()
    object HomeScreen : Destination()
    object LikeScreen : Destination()
    object SuggestScreen : Destination()
    object GuideScreen : Destination()
    data class AddonScreen(val addonId: Int) : Destination()
}