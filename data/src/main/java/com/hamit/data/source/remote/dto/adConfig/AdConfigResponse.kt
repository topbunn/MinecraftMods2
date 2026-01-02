package com.hamit.data.source.remote.dto.adConfig

import kotlinx.serialization.Serializable

@Serializable
data class AdConfigResponse(
    val sdk: AdConfigDto? = null,
)
