package com.l13devstudio.android.ads.interstitial

import android.app.Activity
import android.content.Context
import com.l13devstudio.android.BuildConfig
import com.l13devstudio.android.utills.isShow
import com.l13devstudio.domain.entity.AdEnum
import com.l13devstudio.domain.entity.AppLocation
import com.l13devstudio.domain.entity.adConfig.AdConfigEntity

object InterstitialCoordinator {

    private var initialized = false
    private var activeNetwork: Network = Network.NONE

    private const val SHOW_DELAY_MS = 60_000L
    private var lastShowTime = 0L

    private enum class Network {
        NONE, CAS, YANDEX
    }

    fun init(context: Context, location: AppLocation, config: AdConfigEntity) {
        if (initialized) return
        if (!config.isAdEnabled) return

        initialized = true

        activeNetwork =
            if (!BuildConfig.RUSTORE && location == AppLocation.OTHER) {
//              config.applovinInter?.let {
                    InterstitialCasController.init(context, "TODO")
//              }
                Network.CAS
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
            Network.CAS -> InterstitialCasController.show(activity)
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
            Network.CAS -> InterstitialCasController.deleteCallback()
            Network.YANDEX -> InterstitialYandexController.deleteCallback()
            else -> {}
        }
    }

    fun setCallback(callback: () -> Unit) {
        if (!initialized) return

        when (activeNetwork) {
            Network.CAS -> InterstitialCasController.setCallback(callback)
            Network.YANDEX -> InterstitialYandexController.setCallback(callback)
            else -> {}
        }
    }

    fun start(context: Context) {
        if (!initialized) return
        when (activeNetwork) {
            Network.CAS -> InterstitialCasController.start(context)
            Network.YANDEX -> InterstitialYandexController.start()
            else -> {}
        }
    }

    fun stop() {
        if (!initialized) return
        when (activeNetwork) {
            Network.CAS -> InterstitialCasController.stop()
            Network.YANDEX -> InterstitialYandexController.stop()
            else -> {}
        }
    }

    fun destroy() {
        if (!initialized) return
        when (activeNetwork) {
            Network.CAS -> InterstitialCasController.destroy()
            Network.YANDEX -> InterstitialYandexController.destroy()
            else -> {}
        }
        initialized = false
    }
}
