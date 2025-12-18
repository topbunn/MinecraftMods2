package ru.topbun.buildSrc

data class AddonConfig(
    val applovinSdkKey: String,
    val yandexMetricKey: String,
    val packageName: String,
    val appId: String,
    val appColor: String,
    val percentNative: String,
    val percentInter: String,
    val ruStore: String,
)