package com.l13devstudio.app.app

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.l13devstudio.ad.AdScreen
import com.l13devstudio.addon.AddonScreen
import com.l13devstudio.dashboard.DashboardScreen
import com.l13devstudio.download.DownloadScreen
import com.l13devstudio.faq.FaqScreen
import com.l13devstudio.guide.GuideScreen
import com.l13devstudio.home.HomeScreen
import com.l13devstudio.like.LikeScreen
import com.l13devstudio.loader.LoaderScreen
import com.l13devstudio.navigation.Destination
import com.l13devstudio.suggest.SuggestScreen

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
        register<Destination.FaqScreen> {
            FaqScreen
        }
        register<Destination.AdScreen> { provider ->
            AdScreen(provider.nextDestination)
        }
        register<Destination.DownloadScreen> { provider ->
            DownloadScreen(provider.addon)
        }
        register<Destination.AddonScreen> { provider ->
            AddonScreen(provider.addonId)
        }

    }
}