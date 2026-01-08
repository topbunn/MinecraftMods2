package com.hamit.home

import androidx.compose.foundation.lazy.LazyListState
import com.hamit.domain.entity.addon.AddonEntity

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
    val loadingStatus: AddonLoadingStatus = AddonLoadingStatus.Idle
){

    sealed interface AddonLoadingStatus{

        object Idle: AddonLoadingStatus
        object Loading: AddonLoadingStatus
        object Success: AddonLoadingStatus
        data class Error(val message: String): AddonLoadingStatus

    }

    enum class FilterType{ SORTS, ADDON_TYPES }

}
