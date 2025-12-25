package com.hamit.data.api.dto.location

import com.google.gson.annotations.SerializedName

data class RegionMetadata(
    @SerializedName("country_code")
    val zoneCode: String?
)
