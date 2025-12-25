package com.hamit.data.api.dto.mods

import com.google.gson.annotations.SerializedName
import com.hamit.data.BuildConfig
import com.hamit.domain.entity.app.AppInfoEntity
import com.hamit.domain.entity.app.AppInfoStatusEnum

data class SystemConfigDto(
    val order: Int,
    val packageName: String,
    val logo: String,
    val status: AppInfoStatusEnum,
    val coreParams: CoreParamsDto? = null,
    val translations: List<EntryLabelDto>
)

fun List<SystemConfigDto>.toEntity(applicationId: String) =
    filter { it.status == AppInfoStatusEnum.PUBLISHED && it.packageName != applicationId }
        .sortedBy { it.order }
        .map {
            AppInfoEntity(
                googlePlayLink = "https://play.google.com/store/apps/details?id=" + it.packageName,
                logoLink = BuildConfig.BASE_URL + it.logo,
                title = it.translations.firstOrNull()?.name ?: ""
            )
        }

data class EntryLabelDto(
    val name: String
)

data class CoreParamsDto(
    @SerializedName("isAdsEnabled") val isAdEnabled: Boolean,
    @SerializedName("secondOpenCode") val applovinOpen: String,
    @SerializedName("secondInterCode") val applovinInter: String,
    @SerializedName("secondNativeCode") val applovinNative: String,
    @SerializedName("thirdOpenCode") val yandexOpen: String,
    @SerializedName("thirdInterCode") val yandexInter: String,
    @SerializedName("thirdNativeCode") val yandexNative: String,
)
