package com.youlovehamit.app

import com.hamit.domain.entity.modConfig.ModConfig

class AppModConfigProvider : com.hamit.domain.entity.modConfig.ModConfigProvider {
    override fun getConfig(): com.hamit.domain.entity.modConfig.ModConfig =
        _root_ide_package_.com.topbun.domain.entity.modConfig.ModConfig(
            applovinSdkKey = BuildConfig.APPLOVIN_SDK_KEY,
            metricKey = BuildConfig.METRIC_KEY,
            applicationId = BuildConfig.APPLICATION_ID,
            appId = BuildConfig.APP_ID,
            primaryColor = BuildConfig.PRIMARY_COLOR,
            percentShowNativeAd = BuildConfig.PERCENT_SHOW_NATIVE_AD,
            percentShowInterAd = BuildConfig.PERCENT_SHOW_INTER_AD
        )
}