package com.hamit.data.source.remote.dto.suggest

import com.hamit.domain.entity.suggest.SuggestEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuggestDto(
    @SerialName("email") val email: String,
    @SerialName("url") val link: String,
    @SerialName("description") val desc: String,
){
    companion object{
        fun fromEntity(entity: SuggestEntity) = SuggestDto(entity.email, entity.link, entity.desc)
    }
}
