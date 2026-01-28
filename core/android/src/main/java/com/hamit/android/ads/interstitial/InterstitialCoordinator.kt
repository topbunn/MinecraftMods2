package com.hamit.android.ads.interstitial

import android.app.Activity
import android.content.Context
import com.hamit.android.BuildConfig
import com.hamit.android.utills.isShow
import com.hamit.domain.entity.AdEnum
import com.hamit.domain.entity.AppLocation
import com.hamit.domain.entity.adConfig.AdConfigEntity

object InterstitialCoordinator {

    private var initialized = false
    private var activeNetwork: Network = Network.NONE

    private const val SHOW_DELAY_MS = 60_000L
    private var lastShowTime = 0L

    private enum class Network {
        NONE, APPLOVIN, YANDEX
    }

    fun init(context: Context, location: AppLocation, config: AdConfigEntity) {
        if (initialized) return
        if (!config.isAdEnabled) return

        initialized = true

        activeNetwork =
            if (!BuildConfig.RUSTORE && location == AppLocation.OTHER) {
                config.applovinInter?.let { InterstitialApplovinController.init(context, it) }
                Network.APPLOVIN
            } else {
                config.yandexInter?.let { InterstitialYandexController.init(context, it) }
                Network.YANDEX
            }
    }

    fun show(activity: Activity) {
        if (!initialized) return
        if (!AdEnum.INTER.isShow()) return
        if (!canShow()) return

        when (activeNetwork) {
            Network.APPLOVIN -> InterstitialApplovinController.show()
            Network.YANDEX -> InterstitialYandexController.show(activity)
            else -> return
        }

        lastShowTime = System.currentTimeMillis()
    }

    private fun canShow(): Boolean {
        val now = System.currentTimeMillis()
        return now - lastShowTime >= SHOW_DELAY_MS
    }

    fun deleteCallback() {
        if (!initialized) return

        when (activeNetwork) {
            Network.APPLOVIN -> InterstitialApplovinController.deleteCallback()
            Network.YANDEX -> InterstitialYandexController.deleteCallback()
            else -> {}
        }
    }

    fun setCallback(callback: () -> Unit) {
        if (!initialized) return

        when (activeNetwork) {
            Network.APPLOVIN -> InterstitialApplovinController.setCallback(callback)
            Network.YANDEX -> InterstitialYandexController.setCallback(callback)
            else -> {}
        }
    }

    fun start() {
        if (!initialized) return
        when (activeNetwork) {
            Network.APPLOVIN -> InterstitialApplovinController.start()
            Network.YANDEX -> InterstitialYandexController.start()
            else -> {}
        }
    }

    fun stop() {
        if (!initialized) return
        when (activeNetwork) {
            Network.APPLOVIN -> InterstitialApplovinController.stop()
            Network.YANDEX -> InterstitialYandexController.stop()
            else -> {}
        }
    }

    fun destroy() {
        if (!initialized) return
        when (activeNetwork) {
            Network.APPLOVIN -> InterstitialApplovinController.destroy()
            Network.YANDEX -> InterstitialYandexController.destroy()
            else -> {}
        }
    }
}
