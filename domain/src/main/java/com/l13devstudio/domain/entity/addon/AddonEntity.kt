package com.l13devstudio.domain.entity.addon

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddonEntity(
    val id: Int,
    val name: String,
    val desc: String,
    val preview: String,
    val type: AddonType,
    val isLike: Boolean,
    val images: List<String>,
    val files: List<String>,
    val versions: List<String>,
): Parcelable
