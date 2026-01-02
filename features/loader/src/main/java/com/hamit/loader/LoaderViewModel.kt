package com.hamit.loader

import android.app.Application
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hamit.android.ads.interstitial.InterstitialCoordinator
import com.hamit.android.ads.natives.NativeCoordinator
import com.hamit.data.repository.DataRepository
import com.hamit.data.repository.RegionProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoaderViewModel(
    private val application: Application,
    private val addonRepository: DataRepository,
    private val regionProvider: RegionProvider
) : ScreenModel {

    private val _state = MutableStateFlow(LoaderState())
    val state = _state.asStateFlow()

    private fun simulateLoading() = screenModelScope.launch {
        delay(10000)
        _state.update { it.copy(shouldGoApp = true) }
    }

    fun navigateToApp() {
        _state.update { it.copy(toNavigate = true) }
    }

    private fun preloadAds() = screenModelScope.launch {
        val config = addonRepository.receiveConfig()
        val location = regionProvider.receiveRegion()
        InterstitialCoordinator.init(application.applicationContext, location, config)
        NativeCoordinator.init(application.applicationContext, location, config)
        _state.update { it.copy(adInit = true) }
    }

    init {
        preloadAds()
        simulateLoading()
    }

}