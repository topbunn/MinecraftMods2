package com.hamit.addon

import com.hamit.domain.entity.addon.AddonEntity

data class AddonState(
    val addon: AddonEntity? = null,
    val pathFile: String? = null,
    val shouldOpenIssue: Boolean = false,
    val imageIsExpand: Boolean = false,
    val textIsExpand: Boolean = false,
    val loadAddonState: LoadAddonState = LoadAddonState.Idle,
    val downloadState: DownloadModState = DownloadModState.Idle,
){

    sealed interface LoadAddonState{

        data object Idle: LoadAddonState
        data object Loading: LoadAddonState
        data class Error(val message: String): LoadAddonState
        data object Success: LoadAddonState

    }

    sealed interface DownloadModState{

        data object Idle: DownloadModState
        data class Loading(val progress: Int): DownloadModState
        data class Error(val message: String): DownloadModState
        data object Success: DownloadModState

    }

}
