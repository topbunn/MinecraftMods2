package com.l13devstudio.domain.useCases.region

import com.l13devstudio.domain.repository.RegionRepository

class ReceiveRegionUseCase(
    private val repository: RegionRepository
) {

    suspend operator fun invoke() = repository.receiveRegion()

}