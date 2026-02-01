package com.l13devstudio.domain.useCases.like

import com.l13devstudio.domain.entity.like.LikeEntity
import com.l13devstudio.domain.repository.ConfigRepository
import com.l13devstudio.domain.repository.LikeRepository

class AddLikeUseCase(
    private val repository: LikeRepository
){

    suspend operator fun invoke(like: LikeEntity) = repository.addLike(like)

}