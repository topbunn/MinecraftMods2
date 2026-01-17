package com.hamit.app.app

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.hamit.addon.AddonScreen
import com.hamit.dashboard.DashboardScreen
import com.hamit.download.DownloadScreen
import com.hamit.guide.GuideScreen
import com.hamit.home.HomeScreen
import com.hamit.like.LikeScreen
import com.hamit.loader.LoaderScreen
import com.hamit.navigation.Destination
import com.hamit.suggest.SuggestScreen

fun setupSharedScreens() {
    ScreenRegistry {
        register<Destination.LoaderScreen> {
            LoaderScreen
        }
        register<Destination.DashboardScreen> {
            DashboardScreen
        }
        register<Destination.HomeScreen> {
            HomeScreen
        }
        register<Destination.LikeScreen> {
            LikeScreen
        }
        register<Destination.SuggestScreen> {
            SuggestScreen
        }
        register<Destination.GuideScreen> {
            GuideScreen
        }
        register<Destination.DownloadScreen> { provider ->
            DownloadScreen(provider.files)
        }
        register<Destination.AddonScreen> { provider ->
            AddonScreen(provider.addonId)
        }

    }
}