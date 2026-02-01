package com.l13devstudio.home

sealed interface HomeEvent{
    data class OpenMod(val id: Int): HomeEvent
}
