package com.hamit.like

import androidx.compose.foundation.lazy.LazyListState
import com.hamit.domain.entity.addon.AddonEntity

data class LikeState(
    val addons: List<AddonEntity> = emptyList(),
    val addonListState: LazyListState = LazyListState(),
    val likeTotalCount: Int? = null,
    val shouldOpenAddon: AddonEntity? = null,
    val addonIsEndList: Boolean = false,
    val likeScreenState: LikeScreenState = LikeScreenState.Idle,
){

    sealed interface LikeScreenState{

        object Idle: LikeScreenState
        object Loading: LikeScreenState
        object Success: LikeScreenState
        data class Error(val message: String): LikeScreenState

    }

}
