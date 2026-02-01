package com.l13devstudio.domain.entity.adConfig

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdConfigEntity(
    val isAdEnabled: Boolean,
    val applovinOpen: String?,
    val applovinInter: String?,
    val casNative: String?,
    val yandexOpen: String?,
    val yandexInter: String?,
    val yandexNative: String?,
) : Parcelable