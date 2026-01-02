package com.hamit.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PropertyEntity(
    val isAdEnabled: Boolean,
    val applovinOpen: String?,
    val applovinInter: String?,
    val applovinNative: String?,
    val yandexOpen: String?,
    val yandexInter: String?,
    val yandexNative: String?,
) : Parcelable
