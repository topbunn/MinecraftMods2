package com.hamit.main

import androidx.compose.foundation.lazy.LazyListState
import com.hamit.domain.entity.addon.AddonEntity

data class MainState(
    val mods: List<AddonEntity> = emptyList(),
    val openMod: AddonEntity? = null,
    val search: String = "",
    val modListState: LazyListState = LazyListState(),
    val modSorts: List<ModSortTypeUi> = ModSortTypeUi.entries,
    val modSortSelectedIndex: Int = 0,
    val modTypeUis: List<ModTypeUi> = ModTypeUi.entries,
    val selectedModTypeUi: ModTypeUi = ModTypeUi.ALL,
    val isEndList: Boolean = false,
    val mainScreenState: MainScreenState = MainScreenState.Idle
){

    sealed interface MainScreenState{

        object Idle: MainScreenState
        object Loading: MainScreenState
        object Success: MainScreenState
        data class Error(val message: String): MainScreenState

    }

}
