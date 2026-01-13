package com.hamit.addon

import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.components.addon.AddonListStatusUi

data class AddonState(
    val addon: AddonEntity? = null,
    val otherAddons: List<AddonEntity>? = null,
    val openProblem: Boolean = false,
    val textIsExpand: Boolean = false,
    val loadStatus: AddonListStatusUi = AddonListStatusUi.Idle,
)