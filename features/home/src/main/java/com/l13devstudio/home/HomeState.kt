package com.l13devstudio.home

import androidx.compose.foundation.lazy.LazyListState
import com.l13devstudio.domain.entity.addon.AddonEntity
import com.l13devstudio.domain.entity.AddonListStatusUi
import com.l13devstudio.ui.entity.AddonSortTypeUi
import com.l13devstudio.ui.entity.AddonTypeUi

internal data class HomeState(
    val addons: List<AddonEntity> = emptyList(),
    val query: String = "",
    val sortTypes: List<Int> = AddonSortTypeUi.entries.map { it.stringResourceId },
    val selectedSortTypeIndex: Int = 0,
    val addonTypes : List<Int> = AddonTypeUi.entries.map { it.titleStringRes },
    val selectedAddonTypeIndex: Int = 0,
    val filterIsOpen: Boolean = false,
    val listState: LazyListState = LazyListState(),
    val isEndOfList: Boolean = false,
    val addonStatus: AddonListStatusUi = AddonListStatusUi.Idle
){


    enum class FilterType{ SORTS, ADDON_TYPES }

}
