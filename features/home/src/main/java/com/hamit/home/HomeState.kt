package com.hamit.home

import androidx.compose.foundation.lazy.LazyListState
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.components.addon.AddonListStatusUi

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
