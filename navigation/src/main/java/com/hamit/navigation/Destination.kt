package com.hamit.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider
import com.hamit.domain.entity.addon.AddonEntity

sealed class Destination : ScreenProvider {
    object DashboardScreen : Destination()
    object LoaderScreen : Destination()
    object HomeScreen : Destination()
    object LikeScreen : Destination()
    object SuggestScreen : Destination()
    object GuideScreen : Destination()
    data class DownloadScreen(val addon: AddonEntity) : Destination()
    data class AddonScreen(val addonId: Int) : Destination()
}