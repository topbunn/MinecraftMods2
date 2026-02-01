package com.l13devstudio.domain.useCases.addon

import android.R.attr.type
import com.l13devstudio.domain.entity.addon.AddonSortType
import com.l13devstudio.domain.entity.addon.AddonType
import com.l13devstudio.domain.repository.AddonRepository

class ReceiveAddonListUseCase(
    private val repository: AddonRepository
){

    suspend operator fun invoke(
        q: String,
        offset: Int,
        type: AddonType?,
        sortType: AddonSortType,
        limit: Int = 6,
    ) = repository.receiveAddonList(q, offset, type, sortType, limit)

}