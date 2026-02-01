package com.l13devstudio.data.source.remote.dto.suggest

import com.l13devstudio.domain.entity.suggest.SuggestEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuggestDto(
    @SerialName("email") val email: String,
    @SerialName("description") val desc: String,
){
    companion object{
        fun fromEntity(entity: SuggestEntity) = SuggestDto(entity.email, entity.desc)
    }
}
