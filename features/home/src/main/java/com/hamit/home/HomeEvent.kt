package com.hamit.home

import com.hamit.navigation.Destination

sealed interface HomeEvent{
    data class OpenMod(val destination: Destination): HomeEvent
}
