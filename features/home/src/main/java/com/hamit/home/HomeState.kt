package com.hamit.home

import androidx.compose.foundation.lazy.LazyListState
import com.hamit.domain.entity.addon.AddonEntity

data class HomeState(
    val addons: List<AddonEntity> = emptyList(),
    val shouldAddonOpen: AddonEntity? = null,
    val query: String = "",
    val addonListState: LazyListState = LazyListState(),
    val homeSorts: List<AddonSortTypeUi> = AddonSortTypeUi.entries,
    val addonSortSelectedIndex: Int = 0,
    val addonTypeUis: List<AddonTypeUi> = AddonTypeUi.entries,
    val selectedAddonTypeUi: AddonTypeUi = AddonTypeUi.ALL,
    val isAddonListEnd: Boolean = false,
    val homeScreenState: HomeScreenState = HomeScreenState.Idle
){

    sealed interface HomeScreenState{

        object Idle: HomeScreenState
        object Loading: HomeScreenState
        object Success: HomeScreenState
        data class Error(val message: String): HomeScreenState

    }

}
