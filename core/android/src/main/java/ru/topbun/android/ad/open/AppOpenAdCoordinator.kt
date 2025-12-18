package ru.topbun.android.ad.open

import android.app.Activity
import ru.topbun.android.BuildConfig
import ru.topbun.android.utils.AppLocation
import ru.topbun.domain.entity.ConfigEntity

object AppOpenAdCoordinator {

    private var isInitialized = false
    private var activeProvider: AdProvider = AdProvider.NONE

    private enum class AdProvider {
        NONE, APPLOVIN, YANDEX
    }

    fun init(activity: Activity, location: AppLocation, config: ConfigEntity) {
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
