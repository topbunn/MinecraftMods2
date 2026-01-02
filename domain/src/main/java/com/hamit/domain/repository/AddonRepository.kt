package com.hamit.domain.repository

import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.domain.entity.addon.AddonSortType
import com.hamit.domain.entity.addon.AddonType

interface AddonRepository {

    suspend fun receiveAddonList(
        q: String,
        offset: Int,
        type: AddonType?,
        sortType: AddonSortType,
        limit: Int,
    ): Result<List<AddonEntity>>

    suspend fun receiveAddon(id: Int): Result<AddonEntity>

}