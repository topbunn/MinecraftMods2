package com.hamit.data.api.dto.mods

import com.hamit.domain.entity.addon.AddonType

data class EntryDto(
    val id: Int,
    val category: AddonType,
    val rating: Double,
    val commentCounts: Int,
    val descriptionImages: List<String>,
    val title: String,
    val description: String,
    val image: String,
    val files: List<String>,
    val versions: List<VersionDto>,
)
