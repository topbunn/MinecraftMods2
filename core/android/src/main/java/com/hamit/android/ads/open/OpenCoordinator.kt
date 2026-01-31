package com.hamit.android.ads.open

import android.app.Activity
import com.hamit.android.BuildConfig
import com.hamit.domain.entity.AppLocation
import com.hamit.domain.entity.adConfig.AdConfigEntity

object OpenCoordinator {

    private var initialized = false
    private var activeNetwork: Network = Network.NONE

    private enum class Network {
        NONE, CAS, YANDEX
    }

    fun init(activity: Activity, location: AppLocation, config: AdConfigEntity) {
        if (initialized) return
        if (!config.isAdEnabled) return

        initialized = true

        activeNetwork =
            if (!BuildConfig.RUSTORE && location == AppLocation.OTHER) {
//                config.applovinOpen?.let {
                    OpenCasController.init(activity.applicationContext, "TODO")
//                }
                Network.CAS
            } else {
                config.yandexOpen?.let { OpenYandexController.init(activity.application, it) }
                Network.YANDEX
            }
    }

    fun show(activity: Activity) {
        if (!initialized) return
        when (activeNetwork) {
            Network.CAS -> OpenCasController.show(activity)
            Network.YANDEX -> OpenYandexController.show(activity)
            else -> {}
        }

    }

    fun start(activity: Activity, canShow: Boolean) {
        if (!initialized) return
        when (activeNetwork) {
            Network.CAS -> {
                OpenCasController.load(activity.applicationContext)
                if (canShow){
                    OpenCasController.show(activity)
                }
            }

            Network.YANDEX -> {
                if (canShow) {
                    OpenYandexController.show(activity)
                }
            }
            else -> {}
        }
    }


    fun destroy() {
        if (!initialized) return
        when (activeNetwork) {
            Network.CAS -> OpenCasController.destroy()
            Network.YANDEX -> OpenYandexController.destroy()
            else -> {}
        }
        initialized = false
    }


}
