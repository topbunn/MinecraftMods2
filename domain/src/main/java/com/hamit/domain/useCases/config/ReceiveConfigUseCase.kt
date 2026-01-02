package com.hamit.domain.useCases.config

import com.hamit.domain.repository.ConfigRepository

class ReceiveConfigUseCase(
    private val repository: ConfigRepository
){

    suspend operator fun invoke() = repository.receiveConfig()

}