package com.youlovehamit.app.app

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.hamit.apps.AppsScreen
import com.hamit.detail_mod.DetailModScreen
import com.hamit.favorite.FavoriteScreen
import com.hamit.instruction.InstructionScreen
import com.hamit.main.MainScreen
import com.hamit.navigation.Destination
import com.hamit.splash.SplashScreen
import com.hamit.tabs.TabsScreen

fun setupSharedScreens() {
    ScreenRegistry {
        register<com.hamit.navigation.SharedScreen.TabsScreen> {
            TabsScreen
        }
        register<com.hamit.navigation.SharedScreen.SplashScreen> {
            SplashScreen
        }
        register<com.hamit.navigation.SharedScreen.MainScreen> {
            MainScreen
        }
        register<com.hamit.navigation.SharedScreen.InstructionScreen> {
            InstructionScreen
        }
        register<Destination.FeedbackScreen> {
            AppsScreen
        }
        register<com.hamit.navigation.SharedScreen.FavoriteScreen> {
            FavoriteScreen
        }
        register<Destination.AddonScreen> { provider ->
            DetailModScreen(provider.addonId)
        }
    }
}