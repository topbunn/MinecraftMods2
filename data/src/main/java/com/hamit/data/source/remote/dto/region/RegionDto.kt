package com.hamit.data.source.remote.dto.region

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegionDto(
    @SerialName("country_code") val zoneCode: String?
)
