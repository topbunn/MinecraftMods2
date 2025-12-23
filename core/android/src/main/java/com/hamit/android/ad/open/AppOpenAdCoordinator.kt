package com.hamit.android.ad.open

import android.app.Activity
import com.hamit.android.BuildConfig
import com.hamit.android.utils.AppLocation
import com.hamit.domain.entity.PropertyEntity

object AppOpenAdCoordinator {

    private var isInitialized = false
    private var activeProvider: AdProvider = AdProvider.NONE

    private enum class AdProvider {
        NONE, APPLOVIN, YANDEX
    }

    fun init(activity: Activity, location: AppLocation, config: PropertyEntity) {
        if (isInitialized) return
        if (!config.isAdEnabled) return

        isInitialized = true

        activeProvider =
            if (!BuildConfig.RUSTORE && location == AppLocation.OTHER) {
                config.applovinOpen?.let {
                    AppOpenApplovinController.init(activity, it)
                }
                AdProvider.APPLOVIN
            } else {
                config.yandexOpen?.let {
                    AppOpenYandexController.init(activity.application, it)
                }
                AdProvider.YANDEX
            }
    }

    fun show(activity: Activity) {
        if (!isInitialized) return

        when (activeProvider) {
            AdProvider.APPLOVIN -> AppOpenApplovinController.showIfReady()
            AdProvider.YANDEX -> AppOpenYandexController.show(activity)
            else -> {}
        }
    }

    fun onStart(activity: Activity) {
        if (!isInitialized) return
        when (activeProvider) {
            AdProvider.APPLOVIN -> {
                AppOpenApplovinController.resume()
                AppOpenApplovinController.showIfReady()
            }
            AdProvider.YANDEX -> AppOpenYandexController.show(activity)
            else -> {}
        }
    }

    fun onStop() {
        if (!isInitialized) return
        when (activeProvider) {
            AdProvider.APPLOVIN -> AppOpenApplovinController.pause()
            else -> {}
        }
    }

    fun onDestroy() {
        if (!isInitialized) return
        when (activeProvider) {
            AdProvider.APPLOVIN -> AppOpenApplovinController.destroy()
            AdProvider.YANDEX -> AppOpenYandexController.destroy()
            else -> {}
        }
    }
}
