package com.hamit.android.ads.natives

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hamit.android.BuildConfig
import com.hamit.android.ads.natives.NativeCoordinator.Network.APPLOVIN
import com.hamit.android.ads.natives.NativeCoordinator.Network.NONE
import com.hamit.android.ads.natives.NativeCoordinator.Network.YANDEX
import com.hamit.android.ads.natives.NativeCoordinator.ViewAdType.Fullscreen
import com.hamit.android.ads.natives.NativeCoordinator.ViewAdType.Native
import com.hamit.android.utills.isShow
import com.hamit.domain.entity.AdEnum
import com.hamit.domain.entity.AppLocation
import com.hamit.domain.entity.adConfig.AdConfigEntity

object NativeCoordinator {

    private var initialized = false
    private var activeNetwork: Network = NONE

    private enum class Network {
        NONE, APPLOVIN, YANDEX
    }

    enum class PreloadStatus{
        NONE, PRELOADED, NOT_PRELOADED
    }

    enum class ViewAdType{
        Fullscreen, Native
    }

    fun init(context: Context, location: AppLocation, config: AdConfigEntity) {
        if (initialized) return
        if (!config.isAdEnabled) return

        initialized = true

        activeNetwork =
            if (!BuildConfig.RUSTORE && location == AppLocation.OTHER) {
                config.applovinNative?.let {
//                    NativeApplovinController.init(context, it)
//                    NativeApplovinController.load(context)
                }
                APPLOVIN
            } else {
                config.yandexNative?.let {
                    NativeYandexController.init(context, it)
                    NativeYandexController.load()
                }
                YANDEX
            }


    }

    fun setOnPreload(onPreloaded: (PreloadStatus) -> Unit) {
        when {
            !initialized -> onPreloaded(PreloadStatus.NONE)
            else -> when (activeNetwork) {
//                APPLOVIN -> NativeApplovinController.setCallback(onPreloaded)
                YANDEX -> NativeYandexController.setCallback(onPreloaded)
                else -> onPreloaded(PreloadStatus.NONE)
            }
        }
    }

    fun hasAd(): Boolean = when{
        !initialized -> false
        else -> when (activeNetwork) {
//            APPLOVIN -> NativeApplovinController.hasAd()
            YANDEX -> NativeYandexController.hasAd()
            else -> false
        }
    }

    fun clearOnPreload() {
        if (initialized) return
        when (activeNetwork) {
//            APPLOVIN -> NativeApplovinController.deleteCallback()
            YANDEX -> NativeYandexController.deleteCallback()
            else -> NONE
        }

    }


    @Composable
    fun show(type: ViewAdType, modifier: Modifier = Modifier) {
        if (!initialized) return
        if (AdEnum.NATIVE.isShow()){
            when (activeNetwork) {
//                Network.APPLOVIN -> {
//                    when(type){
//                        Fullscreen -> FullscreenNativeApplovinView()
//                        Native -> NativeApplovinView(modifier = modifier)
//                    }
//                }
                Network.YANDEX -> {
                    when(type){
                        Fullscreen -> FullscreenNativeYandexView()
                        Native -> NativeYandexView(modifier = modifier)
                    }
                }
                else -> {}
            }
        }
    }

    fun destroy() {
        if (!initialized) return
        when (activeNetwork) {
//            Network.APPLOVIN -> NativeApplovinController.destroy()
            Network.YANDEX -> NativeYandexController.destroy()
            else -> {}
        }
        initialized = false
    }
}