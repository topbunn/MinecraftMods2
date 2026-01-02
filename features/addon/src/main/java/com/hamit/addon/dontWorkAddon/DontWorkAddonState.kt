package com.hamit.addon.dontWorkAddon

data class DontWorkAddonState(
    val email: String = "",
    val message: String = "",
    val feedbackState: DontWorkScreenState = DontWorkScreenState.Idle,
){

    sealed interface DontWorkScreenState{
        object Idle: DontWorkScreenState
        object Loading: DontWorkScreenState
        object Success: DontWorkScreenState
        class Error(val message: String): DontWorkScreenState
    }

}
