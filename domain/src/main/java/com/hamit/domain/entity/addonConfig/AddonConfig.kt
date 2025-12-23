package com.hamit.domain.entity.addonConfig

data class AddonConfig(
    val applovinSdkKey: String,
    val metricKey: String,
    val applicationId: String,
    val appId: Int,
    val primaryColor: Int,
    val percentShowNativeAd: Int,
    val percentShowInterAd: Int,
)
