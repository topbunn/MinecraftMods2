package com.hamit.domain.useCases.like

import com.hamit.domain.repository.LikeRepository

class ReceiveLikeAddonsUseCase(
    private val repository: LikeRepository
){

    suspend operator fun invoke(offset: Int, limit: Int = 3) =
        repository.receiveFavoriteAddons(offset, limit)

}