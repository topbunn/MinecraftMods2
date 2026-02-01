package com.l13devstudio.data.source.remote.dto.adConfig

import kotlinx.serialization.Serializable

@Serializable
data class AdConfigResponse(
    val sdk: AdConfigDto? = null,
)
