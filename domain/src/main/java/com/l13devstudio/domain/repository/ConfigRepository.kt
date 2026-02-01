package com.l13devstudio.domain.repository

import com.l13devstudio.domain.entity.adConfig.AdConfigEntity

interface ConfigRepository {

    suspend fun receiveConfig(): AdConfigEntity

}