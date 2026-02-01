package com.l13devstudio.data.source.remote.dto.problem

import com.l13devstudio.domain.entity.problem.ProblemEntity
import kotlinx.serialization.Serializable

@Serializable
data class ProblemDto(
    val text: String,
    val email: String
){
    companion object{
        fun fromEntity(entity: ProblemEntity) = ProblemDto(entity.text, entity.email)
    }

}