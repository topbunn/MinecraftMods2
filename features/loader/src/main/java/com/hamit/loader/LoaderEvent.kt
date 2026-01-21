package com.hamit.loader

sealed interface LoaderEvent {

    data object OpenDashboard: LoaderEvent

}