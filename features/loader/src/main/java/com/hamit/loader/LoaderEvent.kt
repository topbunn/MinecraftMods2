package com.hamit.loader

import com.hamit.navigation.Destination

sealed interface LoaderEvent {

    data class OpenScreen(val destination: Destination): LoaderEvent

}