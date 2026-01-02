package com.hamit.app.app

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.hamit.addon.AddonScreen
import com.hamit.dashboard.DashboardScreen
import com.hamit.guide.GuideScreen
import com.hamit.home.HomeScreen
import com.hamit.like.LikeScreen
import com.hamit.loader.LoaderScreen
import com.hamit.navigation.Destination

fun setupSharedScreens() {
    ScreenRegistry {
        register<Destination.DashboardScreen> {
            DashboardScreen
        }
        register<Destination.LoaderScreen> {
            LoaderScreen
        }
        register<Destination.HomeScreen> {
            HomeScreen
        }
        register<Destination.GuideScreen> {
            GuideScreen
        }
        register<Destination.LikeScreen> {
            LikeScreen
        }
        register<Destination.AddonScreen> { provider ->
            AddonScreen(provider.addonId)
        }

    }
}