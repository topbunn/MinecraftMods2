package com.hamit.domain.useCases.addon

import android.R.attr.type
import com.hamit.domain.entity.addon.AddonSortType
import com.hamit.domain.entity.addon.AddonType
import com.hamit.domain.repository.AddonRepository

class ReceiveAddonUseCase(
    private val repository: AddonRepository
){

    suspend operator fun invoke(id: Int) = repository.receiveAddon(id)

}