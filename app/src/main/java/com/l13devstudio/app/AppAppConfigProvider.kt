package com.l13devstudio.app

import com.l13devstudio.domain.entity.appConfig.AppConfig

class AppAppConfigProvider : com.l13devstudio.domain.entity.appConfig.AppConfigProvider {
    override fun getConfig(): AppConfig = AppConfig(
        metricKey = BuildConfig.METRIC_KEY,
        applicationId = BuildConfig.APPLICATION_ID,
        appId = BuildConfig.APP_ID,
        primaryColor = BuildConfig.PRIMARY_COLOR,
        percentShowNativeAd = BuildConfig.PERCENT_SHOW_NATIVE_AD,
        percentShowInterAd = BuildConfig.PERCENT_SHOW_INTER_AD
    )
}