package com.hamit.domain.useCases.region

import com.hamit.domain.repository.RegionRepository

class ReceiveRegionUseCase(
    private val repository: RegionRepository
) {

    suspend operator fun invoke() = repository.receiveRegion()

}