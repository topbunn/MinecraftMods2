package com.hamit.app

import com.hamit.domain.entity.addonConfig.AddonConfig

class AppAddonConfigProvider : com.hamit.domain.entity.addonConfig.AddonConfigProvider {
    override fun getConfig(): com.hamit.domain.entity.addonConfig.AddonConfig = AddonConfig(
        applovinSdkKey = BuildConfig.APPLOVIN_SDK_KEY,
        metricKey = BuildConfig.METRIC_KEY,
        applicationId = BuildConfig.APPLICATION_ID,
        appId = BuildConfig.APP_ID,
        primaryColor = BuildConfig.PRIMARY_COLOR,
        percentShowNativeAd = BuildConfig.PERCENT_SHOW_NATIVE_AD,
        percentShowInterAd = BuildConfig.PERCENT_SHOW_INTER_AD
    )
}