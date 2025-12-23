package com.hamit.android.ad.inter

import android.app.Activity
import android.content.Context
import com.hamit.android.BuildConfig
import com.hamit.android.utils.AppLocation
import com.hamit.domain.entity.PropertyEntity

object InterstitialAdCoordinator {

    private var isInitialized = false
    private var activeProvider: AdProvider = AdProvider.NONE

    private enum class AdProvider {
        NONE, APPLOVIN, YANDEX
    }

    fun init(context: Context, location: AppLocation, config: PropertyEntity) {
        if (isInitialized) return
        if (!config.isAdEnabled) return

        isInitialized = true

        activeProvider =
            if (!BuildConfig.RUSTORE && location == AppLocation.OTHER) {
                config.applovinInter?.let { InterstitialApplovinController.init(context, it) }
                AdProvider.APPLOVIN
            } else {
                config.yandexInter?.let { InterstitialYandexController.init(context, it) }
                AdProvider.YANDEX
            }
    }

    fun show(activity: Activity) {
        if (!isInitialized) return

        when (activeProvider) {
            AdProvider.APPLOVIN -> InterstitialApplovinController.show()
            AdProvider.YANDEX   -> InterstitialYandexController.show(activity)
            else -> {}
        }
    }

    fun clearCallback() {
        if (!isInitialized) return

        when (activeProvider) {
            AdProvider.APPLOVIN -> InterstitialApplovinController.removeAdReadyListener()
            AdProvider.YANDEX   -> InterstitialYandexController.removeAdReadyListener()
            else -> {}
        }
    }

    fun setOnAdReadyCallback(callback: () -> Unit) {
        if (!isInitialized) return

        when (activeProvider) {
            AdProvider.APPLOVIN -> InterstitialApplovinController.setAdReadyListener(callback)
            AdProvider.YANDEX   -> InterstitialYandexController.setAdReadyListener(callback)
            else -> {}
        }
    }

    fun onStart() {
        if (!isInitialized) return

        when (activeProvider) {
            AdProvider.APPLOVIN -> InterstitialApplovinController.resume()
            AdProvider.YANDEX   -> InterstitialYandexController.resume()
            else -> {}
        }
    }

    fun onStop() {
        if (!isInitialized) return

        when (activeProvider) {
            AdProvider.APPLOVIN -> InterstitialApplovinController.pause()
            AdProvider.YANDEX   -> InterstitialYandexController.pause()
            else -> {}
        }
    }

    fun onDestroy() {
        if (!isInitialized) return

        when (activeProvider) {
            AdProvider.APPLOVIN -> InterstitialApplovinController.destroy()
            AdProvider.YANDEX   -> InterstitialYandexController.destroy()
            else -> {}
        }
    }
}
