package com.hamit.app.app

import android.content.Context
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinPrivacySettings
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkInitializationConfiguration
import com.hamit.app.BuildConfig

fun Context.setupApplovin() {
    val initConfig = AppLovinSdkInitializationConfiguration.builder(BuildConfig.APPLOVIN_SDK_KEY)
        .setMediationProvider(AppLovinMediationProvider.MAX)
        .build()

    AppLovinPrivacySettings.setHasUserConsent(true, this)
    AppLovinPrivacySettings.setDoNotSell(false, this)

    AppLovinSdk.getInstance(this).apply {
        settings.setVerboseLogging(true)
        initialize(initConfig) {}
    }

}