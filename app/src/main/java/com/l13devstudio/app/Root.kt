package com.l13devstudio.app

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.l13devstudio.ad.AdScreen
import com.l13devstudio.android.ads.open.OpenCoordinator
import com.l13devstudio.navigation.Destination
import com.l13devstudio.ui.theme.AppTheme
import com.l13devstudio.ui.theme.LocalAppColors


@Composable
fun Root() {
    AppTheme{
        val colors = LocalAppColors.current
        val firstScreen = rememberScreen(Destination.LoaderScreen)
        val systemUi = rememberSystemUiController()
        systemUi.setStatusBarColor(Color.Transparent, !isSystemInDarkTheme())
        Box(
            Modifier.fillMaxSize().background(colors.background)
        ){
            Navigator(firstScreen) { navigator ->
                val activity = LocalActivity.currentOrThrow
                val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

                DisposableEffect(lifecycleOwner, navigator) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_START) {
                            val canShow = navigator.lastItemOrNull !is AdScreen
                            OpenCoordinator.start(activity, canShow)
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                CurrentScreen()
            }
        }
    }
}