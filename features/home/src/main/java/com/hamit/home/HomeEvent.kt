package com.hamit.home

sealed interface HomeEvent{
    data class OpenMod(val id: Int): HomeEvent
}
