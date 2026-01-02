package com.hamit.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
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
import com.hamit.data.database.entity.LikeEntity
import com.hamit.data.repository.DataRepository
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.home.HomeState.HomeScreenState

class HomeViewModel(
    private val repo: DataRepository
) : ScreenModel  {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private var loadModsJob: Job? = null

    fun shouldOpenAddon(mod: AddonEntity?) = _state.update { it.copy(shouldAddonOpen = mod) }

    fun switchLike(mod: AddonEntity) = screenModelScope.launch{
        val favorite = LikeEntity(
            addonId = mod.id,
            isActive = !mod.isLike
        )
        repo.addLike(favorite)
        val newMods = _state.value.addons.map {
            if (it.id == favorite.addonId) it.copy(isLike = favorite.isActive) else it
        }
        _state.update {
            it.copy(addons = newMods)
        }
    }

    fun changeQuery(value: String) = _state.update { it.copy(query = value) }
    fun changeAddonSort(selectedIndex: Int) = _state.update { it.copy(addonSortSelectedIndex = selectedIndex) }
    fun changeSortType(addonTypeUi: AddonTypeUi) = _state.update { it.copy(selectedAddonTypeUi = addonTypeUi) }


    fun handlingStateChanged() {
        _state
            .map { listOf(it.query, it.addonSortSelectedIndex, it.selectedAddonTypeUi) }
            .distinctUntilChanged()
            .drop(1)
            .onEach {
                refreshMods()
            }
            .launchIn(screenModelScope)
    }

    fun refreshMods() {
        _state.update {
            it.copy(addons = emptyList(), homeScreenState = HomeScreenState.Idle, isAddonListEnd = false)
        }
    }

    fun loadMods(){
        loadModsJob?.cancel()
        loadModsJob = screenModelScope.launch{
            _state.update { it.copy(homeScreenState = HomeScreenState.Loading) }
            val result = repo.getMods(
                q = _state.value.query,
                offset = _state.value.addons.size,
                type = _state.value.selectedAddonTypeUi.toAddonSortType(),
                sortType = _state.value.homeSorts[state.value.addonSortSelectedIndex].toAddonSortType()
            )
            result.onSuccess { mods ->
                _state.update {
                    it.copy(
                        addons = it.addons + mods,
                        isAddonListEnd = mods.isEmpty(),
                        homeScreenState = HomeScreenState.Success
                    )
                }
            }.onFailure { error ->
                error.printStackTrace()
                _state.update { it.copy(homeScreenState = HomeScreenState.Error("Loading error. Check Internet connection")) }
            }
        }
    }

}