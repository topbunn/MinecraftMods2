package com.l13devstudio.domain.useCases.like

import com.l13devstudio.domain.repository.ConfigRepository
import com.l13devstudio.domain.repository.LikeRepository

class ReceiveLikeTotalSizeUseCase(
    private val repository: LikeRepository
){

    suspend operator fun invoke() = repository.receiveLikeTotalSize()

}