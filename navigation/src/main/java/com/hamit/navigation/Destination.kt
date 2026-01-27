package com.hamit.navigation

import android.os.Parcelable
import cafe.adriel.voyager.core.registry.ScreenProvider
import com.hamit.domain.entity.addon.AddonEntity
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Destination : ScreenProvider, Parcelable {
    object DashboardScreen : Destination()
    object LoaderScreen : Destination()
    object HomeScreen : Destination()
    object LikeScreen : Destination()
    object SuggestScreen : Destination()
    object GuideScreen : Destination()
    object FaqScreen : Destination()
    data class AdScreen(val nextDestination: Destination) : Destination()
    data class DownloadScreen(val addon: AddonEntity) : Destination()
    data class AddonScreen(val addonId: Int) : Destination()
}