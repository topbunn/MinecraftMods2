package com.hamit.data.source.remote.dto.problem

import com.hamit.domain.entity.problem.ProblemEntity
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