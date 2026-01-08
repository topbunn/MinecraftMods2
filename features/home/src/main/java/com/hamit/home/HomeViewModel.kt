package com.hamit.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hamit.domain.useCases.addon.ReceiveAddonListUseCase
import com.hamit.home.HomeState.FilterType.ADDON_TYPES
import com.hamit.home.HomeState.FilterType.SORTS
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val receiveAddonListUseCase: ReceiveAddonListUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private var addonJob: Job? = null

    fun loadAddons() = with(_state.value){
        addonJob?.cancel()
        addonJob = screenModelScope.launch{
            _state.update { it.copy(loadingStatus = HomeState.AddonLoadingStatus.Loading) }
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
                        loadingStatus = HomeState.AddonLoadingStatus.Success
                    )
                }
            }.onFailure { error ->
                error.printStackTrace()
                _state.update { it.copy(loadingStatus = HomeState.AddonLoadingStatus.Error("")) }
            }
        }
    }

    fun handleChangeState() {
        _state
            .map { listOf(it.query, it.selectedSortTypeIndex, it.selectedAddonTypeIndex) }
            .distinctUntilChanged()
            .drop(1)
            .onEach {
                refreshMods()
            }
            .launchIn(screenModelScope)
    }

    fun refreshMods() {
        _state.update {
            it.copy(addons = emptyList(), loadingStatus = HomeState.AddonLoadingStatus.Idle, isEndOfList = false)
        }
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

}