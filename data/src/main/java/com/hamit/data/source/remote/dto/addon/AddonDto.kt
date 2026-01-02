package com.hamit.data.source.remote.dto.addon

import com.hamit.domain.entity.addon.AddonType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddonDto(
    @SerialName("id") val id: Int,
    @SerialName("category") val category: AddonType,
    @SerialName("rating") val rating: Double,
    @SerialName("commentCounts") val commentCounts: Int,
    @SerialName("descriptionImages") val descriptionImages: List<String>,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("image") val image: String,
    @SerialName("files") val files: List<String>,
    @SerialName("versions") val versions: List<AddonVersionDto>,
)
