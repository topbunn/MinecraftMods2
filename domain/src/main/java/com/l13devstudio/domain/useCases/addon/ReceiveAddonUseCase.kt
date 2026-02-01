package com.l13devstudio.domain.useCases.addon

import android.R.attr.type
import com.l13devstudio.domain.entity.addon.AddonSortType
import com.l13devstudio.domain.entity.addon.AddonType
import com.l13devstudio.domain.repository.AddonRepository

class ReceiveAddonUseCase(
    private val repository: AddonRepository
){

    suspend operator fun invoke(id: Int) = repository.receiveAddon(id)

}