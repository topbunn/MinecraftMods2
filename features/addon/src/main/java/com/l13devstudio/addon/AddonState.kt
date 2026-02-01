package com.l13devstudio.addon

import com.l13devstudio.domain.entity.AddonListStatusUi
import com.l13devstudio.domain.entity.addon.AddonEntity

data class AddonState(
    val addon: AddonEntity? = null,
    val otherAddons: List<AddonEntity>? = null,
    val openProblem: Boolean = false,
    val textIsExpand: Boolean = false,
    val loadStatus: AddonListStatusUi = AddonListStatusUi.Idle,
)