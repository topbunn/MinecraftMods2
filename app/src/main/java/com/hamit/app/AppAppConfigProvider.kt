package com.hamit.app

import com.hamit.domain.entity.appConfig.AppConfig

class AppAppConfigProvider : com.hamit.domain.entity.appConfig.AppConfigProvider {
    override fun getConfig(): com.hamit.domain.entity.appConfig.AppConfig = AppConfig(
        applovinSdkKey = BuildConfig.CAS_SDK_KEY,
        metricKey = BuildConfig.METRIC_KEY,
        applicationId = BuildConfig.APPLICATION_ID,
        appId = BuildConfig.APP_ID,
        primaryColor = BuildConfig.PRIMARY_COLOR,
        percentShowNativeAd = BuildConfig.PERCENT_SHOW_NATIVE_AD,
        percentShowInterAd = BuildConfig.PERCENT_SHOW_INTER_AD
    )
}