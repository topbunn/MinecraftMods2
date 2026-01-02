package com.hamit.domain.repository

import com.hamit.domain.entity.adConfig.AdConfigEntity

interface ConfigRepository {

    suspend fun receiveConfig(): AdConfigEntity

}