package com.hamit.domain.useCases.like

import com.hamit.domain.entity.like.LikeEntity
import com.hamit.domain.repository.ConfigRepository
import com.hamit.domain.repository.LikeRepository

class AddLikeUseCase(
    private val repository: LikeRepository
){

    suspend operator fun invoke(like: LikeEntity) = repository.addLike(like)

}