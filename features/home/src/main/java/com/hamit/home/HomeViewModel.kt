package com.hamit.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hamit.android.ads.natives.NativeCoordinator
import com.hamit.domain.entity.AddonListStatusUi
import com.hamit.domain.entity.AppExceptionType
import com.hamit.domain.entity.Maintenance
import com.hamit.domain.entity.NoInternet
import com.hamit.domain.useCases.addon.ReceiveAddonListUseCase
import com.hamit.home.HomeState.FilterType.ADDON_TYPES
import com.hamit.home.HomeState.FilterType.SORTS
import com.hamit.navigation.Destination
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val receiveAddonListUseCase: ReceiveAddonListUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _events = Channel<HomeEvent>()
    val events get() = _events.receiveAsFlow()

    private var addonJob: Job? = null

    fun loadAddons() = with(_state.value){
        addonJob?.cancel()
        addonJob = screenModelScope.launch{
            _state.update { it.copy(addonStatus = AddonListStatusUi.Loading) }
            val result = receiveAddonListUseCase(
                q = query,
                offset = addons.size,
                type = AddonTypeUi.entries[selectedAddonTypeIndex].toAddonType(),
                sortType = AddonSortTypeUi.entries[selectedSortTypeIndex].toAddonSortType()
            )
            result.onSuccess { addons ->
                _state.update {
                    it.copy(
                        addons = it.addons + addons,
                        isEndOfList = addons.isEmpty(),
                        addonStatus = AddonListStatusUi.Success
                    )
                }
            }.onFailure { error ->
                error.printStackTrace()

                val type = when (error) {
                    NoInternet -> AppExceptionType.NoInternet
                    Maintenance -> AppExceptionType.Maintenance
                    else -> AppExceptionType.Error
                }
                _state.update { it.copy(addonStatus = AddonListStatusUi.Error(type)) }
            }
        }
    }

    
    fun handleChangeState() {
        _state
            .map { listOf(it.query, it.selectedSortTypeIndex, it.selectedAddonTypeIndex) }
            .distinctUntilChanged()
            .drop(1)
            .onEach {
                refreshAddons()
            }
            .launchIn(screenModelScope)
    }

    fun refreshAddons() {
        addonJob?.cancel()

        _state.update {
            it.copy(
                addons = emptyList(),
                isEndOfList = false,
                addonStatus = AddonListStatusUi.Loading
            )
        }

        loadAddons()
    }

    fun changeFilterValue(selectedIndex: Int, type: HomeState.FilterType){
        _state.update {
            when(type){
                SORTS -> it.copy(selectedSortTypeIndex = selectedIndex)
                ADDON_TYPES -> it.copy(selectedAddonTypeIndex = selectedIndex)
            }
        }
    }

    fun changeFilterDialog(status: Boolean) = _state.update { it.copy(filterIsOpen = status) }
    fun changeQuery(q: String) = _state.update { it.copy(query = q) }

    fun openAddon(id: Int) = screenModelScope.launch {
        val addon = Destination.AddonScreen(id)
        val destination = if (NativeCoordinator.hasAd()) Destination.AdScreen(addon) else addon
        _events.send(HomeEvent.OpenMod(destination))
    }

}