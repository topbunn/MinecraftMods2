package com.l13devstudio.domain.repository

import com.l13devstudio.domain.entity.addon.AddonEntity
import com.l13devstudio.domain.entity.addon.AddonSortType
import com.l13devstudio.domain.entity.addon.AddonType

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