package com.hamit.data.source.remote.dto.addon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddonTranslationDto(
    @SerialName("description") val description: String
)
