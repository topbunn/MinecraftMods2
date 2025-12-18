package ru.topbun.android.ad.natives

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.topbun.android.BuildConfig
import ru.topbun.android.utils.AppLocation
import ru.topbun.domain.entity.ConfigEntity

object NativeAdCoordinator {

    private var isInitialized = false
    private var activeProvider: AdProvider = AdProvider.NONE

    private enum class AdProvider {
        NONE, APPLOVIN, YANDEX
    }

    fun init(context: Context, location: AppLocation, config: ConfigEntity) {
        if (isInitialized) return
        if (!config.isAdEnabled) return

        isInitialized = true

        activeProvider =
            if (!BuildConfig.RUSTORE && location == AppLocation.OTHER) {
                config.applovinNative?.let {
                    NativeApplovinController.init(context, it)
                    NativeApplovinController.preload(context)
                }
                AdProvider.APPLOVIN
            } else {
                config.yandexNative?.let {
                    NativeYandexController.init(context, it)
                    NativeYandexController.preload()
                }
                AdProvider.YANDEX
            }
    }

    @Composable
    fun show(modifier: Modifier = Modifier) {
        if (!isInitialized) return
        when (activeProvider) {
            AdProvider.APPLOVIN -> NativeApplovinView(modifier = modifier)
            AdProvider.YANDEX -> NativeYandexView(modifier = modifier)
            else -> {}
        }
    }

    fun onDestroy() {
        if (!isInitialized) return
        when (activeProvider) {
            AdProvider.APPLOVIN -> NativeApplovinController.destroy()
            AdProvider.YANDEX -> NativeYandexController.destroy()
            else -> {}
        }
    }
}
