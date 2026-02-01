package com.l13devstudio.domain.repository

import com.l13devstudio.domain.entity.AppLocation

interface RegionRepository {

    suspend fun receiveRegion(): AppLocation

}