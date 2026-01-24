package com.hamit.loader

import android.app.Application
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hamit.android.ads.interstitial.InterstitialCoordinator
import com.hamit.android.ads.natives.NativeCoordinator
import com.hamit.android.ads.natives.NativeCoordinator.PreloadStatus.NONE
import com.hamit.android.ads.natives.NativeCoordinator.PreloadStatus.PRELOADED
import com.hamit.domain.useCases.config.ReceiveConfigUseCase
import com.hamit.domain.useCases.region.ReceiveRegionUseCase
import com.hamit.navigation.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoaderViewModel(
    private val application: Application,
    private val receiveConfigUseCase: ReceiveConfigUseCase,
    private val receiveRegionUseCase: ReceiveRegionUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(LoaderState())
    val state get() = _state.asStateFlow()

    private val _events = Channel<LoaderEvent>()
    val events get() = _events.receiveAsFlow()

    private fun simulateLoading() = screenModelScope.launch {
        delay(10000)
        _state.update { it.copy(nativePreloadComplete = true) }
    }

    fun navigateToDashboard() = screenModelScope.launch{
        val dashboard = Destination.DashboardScreen
        val destination = if (NativeCoordinator.hasAd()) Destination.AdScreen(dashboard) else dashboard
        _events.send(LoaderEvent.OpenScreen(destination))
        InterstitialCoordinator.deleteCallback()
    }


    private fun preloadAds() = screenModelScope.launch {
        val config = receiveConfigUseCase()
        val location = receiveRegionUseCase()
        NativeCoordinator.init(application.applicationContext, location, config)
        NativeCoordinator.setOnPreload {
            when(it){
                NONE, PRELOADED -> {
                    _state.update { it.copy(nativePreloadComplete = true) }
                    NativeCoordinator.clearOnPreload()
                }
                else -> {}
            }
        }
    }

    init {
        preloadAds()
        simulateLoading()
    }

}