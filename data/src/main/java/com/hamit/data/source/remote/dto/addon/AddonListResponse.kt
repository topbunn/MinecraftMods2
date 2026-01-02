package com.hamit.data.source.remote.dto.addon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddonListResponse(
    @SerialName("count") val count: Int,
    @SerialName("mods") val items: List<AddonDto>
)
