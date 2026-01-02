package com.hamit.domain.useCases.like

import com.hamit.domain.repository.ConfigRepository
import com.hamit.domain.repository.LikeRepository

class ReceiveLikeTotalSizeUseCase(
    private val repository: LikeRepository
){

    suspend operator fun invoke() = repository.receiveLikeTotalSize()

}