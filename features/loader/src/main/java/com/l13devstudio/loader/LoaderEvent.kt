package com.l13devstudio.loader

import com.l13devstudio.navigation.Destination

sealed interface LoaderEvent {

    data class OpenScreen(val destination: Destination): LoaderEvent

}