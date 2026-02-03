package com.l13devstudio.data.source.remote.dto.adConfig

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdConfigDto(
    @SerialName("isAdsEnabled") val isAdEnabled: Boolean,
    @SerialName("secondOpenCode") val applovinOpen: String?,
    @SerialName("secondInterCode") val applovinInter: String?,
    @SerialName("secondNativeCode") val applovinNative: String?,
    @SerialName("thirdOpenCode") val yandexOpen: String?,
    @SerialName("thirdInterCode") val yandexInter: String?,
    @SerialName("thirdNativeCode") val yandexNative: String?,
)
