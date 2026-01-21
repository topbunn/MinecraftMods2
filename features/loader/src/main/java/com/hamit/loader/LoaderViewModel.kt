package com.hamit.loader

import android.app.Application
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.loader.content.Loader
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hamit.android.ads.interstitial.InterstitialCoordinator
import com.hamit.android.ads.natives.NativeCoordinator
import com.hamit.domain.useCases.config.ReceiveConfigUseCase
import com.hamit.domain.useCases.region.ReceiveRegionUseCase
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

    private val _events = Channel<LoaderEvent>()
    val events get() = _events.receiveAsFlow()

    private fun simulateLoading() = screenModelScope.launch {
        delay(10000)
        navigateToDashboard()
    }

    private fun navigateToDashboard() = screenModelScope.launch{
        _events.send(LoaderEvent.OpenDashboard)
        InterstitialCoordinator.deleteCallback()
    }

    private fun preloadAds() = screenModelScope.launch {
        val config = receiveConfigUseCase()
        val location = receiveRegionUseCase()
        InterstitialCoordinator.init(application.applicationContext, location, config)
        NativeCoordinator.init(application.applicationContext, location, config)
        InterstitialCoordinator.setCallback {
            navigateToDashboard()
        }
    }

    init {
        preloadAds()
        simulateLoading()
    }

}