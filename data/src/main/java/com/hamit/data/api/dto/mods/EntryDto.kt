package com.hamit.data.api.dto.mods

import com.hamit.domain.entity.addon.AddonType
import kotlinx.serialization.SerialName

data class EntryDto(
    @SerialName("id") val id: Int,
    @SerialName("category") val category: AddonType,
    @SerialName("rating") val rating: Double,
    @SerialName("commentCounts") val commentCounts: Int,
    @SerialName("descriptionImages") val descriptionImages: List<String>,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("image") val image: String,
    @SerialName("files") val files: List<String>,
    @SerialName("versions") val versions: List<VersionDto>,
)
