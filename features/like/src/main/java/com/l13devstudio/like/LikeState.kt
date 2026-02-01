package com.l13devstudio.like

import androidx.compose.foundation.lazy.LazyListState
import com.l13devstudio.domain.entity.addon.AddonEntity
import com.l13devstudio.domain.entity.AddonListStatusUi

data class LikeState(
    val addons: List<AddonEntity> = emptyList(),
    val addonListState: LazyListState = LazyListState(),
    val likeTotalCount: Int? = null,
    val openAddon: Int? = null,
    val addonIsEndList: Boolean = false,
    val listState: LazyListState = LazyListState(),
    val addonStatus: AddonListStatusUi = AddonListStatusUi.Idle
){


}
