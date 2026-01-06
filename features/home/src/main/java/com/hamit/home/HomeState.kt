package com.hamit.home

internal data class HomeState(
    val sortTypes: List<Int> = AddonSortTypeUi.entries.map { it.stringResourceId },
    val selectedSortTypeIndex: Int = 0,
    val addonTypes : List<Int> = AddonTypeUi.entries.map { it.titleStringRes },
    val selectedAddonTypeIndex: Int = 0,
    val filterIsOpen: Boolean = false,
){

    enum class FilterType{ SORTS, ADDON_TYPES }

}
