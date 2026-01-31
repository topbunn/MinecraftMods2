package com.hamit.buildSrc

data class AddonConfig(
    val yandexMetricKey: String,
    val packageName: String,
    val appId: String,
    val appColor: String,
    val percentNative: String,
    val percentInter: String,
    val ruStore: String,
)