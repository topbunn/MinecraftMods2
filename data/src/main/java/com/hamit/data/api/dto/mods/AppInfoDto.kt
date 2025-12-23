package com.hamit.data.api.dto.mods

import com.google.gson.annotations.SerializedName
import com.hamit.data.BuildConfig
import com.hamit.domain.entity.app.AppInfoEntity
import com.hamit.domain.entity.app.AppInfoStatusEnum

data class AppInfoDto(
    val order: Int,
    val packageName: String,
    val logo: String,
    val status: AppInfoStatusEnum,
    val sdk: SdkInfoDto? = null,
    val translations: List<AppTranslationDto>
)

fun List<AppInfoDto>.toEntity(applicationId: String) =
    filter { it.status == AppInfoStatusEnum.PUBLISHED && it.packageName != applicationId }
        .sortedBy { it.order }
        .map {
            AppInfoEntity(
                googlePlayLink = "https://play.google.com/store/apps/details?id=" + it.packageName,
                logoLink = BuildConfig.BASE_URL + it.logo,
                title = it.translations.firstOrNull()?.name ?: ""
            )
        }

data class AppTranslationDto(
    val name: String
)

data class SdkInfoDto(
    @SerializedName("isAdsEnabled") val isAdEnabled: Boolean,
    @SerializedName("secondOpenCode") val applovinOpen: String,
    @SerializedName("secondInterCode") val applovinInter: String,
    @SerializedName("secondNativeCode") val applovinNative: String,
    @SerializedName("thirdOpenCode") val yandexOpen: String,
    @SerializedName("thirdInterCode") val yandexInter: String,
    @SerializedName("thirdNativeCode") val yandexNative: String,
)
