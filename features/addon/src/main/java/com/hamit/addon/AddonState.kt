package com.hamit.addon

import com.hamit.domain.entity.AddonListStatusUi
import com.hamit.domain.entity.addon.AddonEntity

data class AddonState(
    val addon: AddonEntity? = null,
    val otherAddons: List<AddonEntity>? = null,
    val openProblem: Boolean = false,
    val textIsExpand: Boolean = false,
    val loadStatus: AddonListStatusUi = AddonListStatusUi.Idle,
)