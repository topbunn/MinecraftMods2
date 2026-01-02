package com.hamit.domain.useCases.like

import com.hamit.domain.repository.ConfigRepository
import com.hamit.domain.repository.LikeRepository

class ReceiveFavoriteAddonsUseCase(
    private val repository: LikeRepository
){

    suspend operator fun invoke(offset: Int, limit: Int = 6) =
        repository.receiveFavoriteAddons(offset, limit)

}