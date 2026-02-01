package com.l13devstudio.domain.useCases.like

import com.l13devstudio.domain.repository.LikeRepository

class ReceiveLikeAddonsUseCase(
    private val repository: LikeRepository
){

    suspend operator fun invoke(offset: Int, limit: Int = 3) =
        repository.receiveFavoriteAddons(offset, limit)

}