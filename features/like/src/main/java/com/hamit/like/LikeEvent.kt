package com.hamit.like

sealed interface LikeEvent{
    data class OpenMod(val id: Int): LikeEvent
}
