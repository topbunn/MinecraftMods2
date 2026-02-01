package com.l13devstudio.domain.useCases.config

import com.l13devstudio.domain.repository.ConfigRepository

class ReceiveConfigUseCase(
    private val repository: ConfigRepository
){

    suspend operator fun invoke() = repository.receiveConfig()

}