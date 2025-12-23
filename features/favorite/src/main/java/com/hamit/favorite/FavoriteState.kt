package com.hamit.favorite

import androidx.compose.foundation.lazy.LazyListState
import com.hamit.domain.entity.addon.AddonEntity

data class FavoriteState(
    val mods: List<AddonEntity> = emptyList(),
    val modListState: LazyListState = LazyListState(),
    val favoriteSize: Int? = null,
    val openMod: AddonEntity? = null,
    val isEndList: Boolean = false,
    val favoriteScreenState: FavoriteScreenState = FavoriteScreenState.Idle,
){

    sealed interface FavoriteScreenState{

        object Idle: FavoriteScreenState
        object Loading: FavoriteScreenState
        object Success: FavoriteScreenState
        data class Error(val message: String): FavoriteScreenState

    }

}
