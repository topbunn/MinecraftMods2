package com.hamit.domain.repository

import com.hamit.domain.entity.AppLocation

interface RegionRepository {

    suspend fun receiveRegion(): AppLocation

}