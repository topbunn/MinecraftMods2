package com.hamit.domain.entity.addon

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddonEntity(
    val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val category: AddonType,
    val isLike: Boolean,
    val rating: Double,
    val commentCounts: Int,
    val descriptionImages: List<String>,
    val files: List<String>,
    val versions: List<String>,
): Parcelable
