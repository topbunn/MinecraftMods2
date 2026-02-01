package com.l13devstudio.data.source.remote.dto.region

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegionDto(
    @SerialName("country_code") val zoneCode: String?
)
