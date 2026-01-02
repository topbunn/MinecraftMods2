package com.hamit.like

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hamit.data.database.entity.LikeEntity
import com.hamit.data.repository.DataRepository
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.like.LikeState.LikeScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LikeViewModel(
    private val repo: DataRepository
) : ScreenModel {

    private val _state = MutableStateFlow(LikeState())
    val state = _state.asStateFlow()

    fun switchLike(addon: AddonEntity) = screenModelScope.launch {
        val like = LikeEntity(addonId = addon.id, isActive = false)
        repo.addLike(like)
        _state.update {
            val newAddons = it.addons.toMutableList()
            newAddons.removeIf { it.id == addon.id }
            it.copy(addons = newAddons)
        }
        getLikeTotalCount()
    }

    fun reloadMods() {
        _state.update { LikeState() }
        getLikeTotalCount()
    }

    fun shouldOpenAddon(addon: AddonEntity?) = _state.update { it.copy(shouldOpenAddon = addon) }

    fun receiveAddons() = screenModelScope.launch {
        _state.update { it.copy(likeScreenState = LikeScreenState.Loading) }
        val result = repo.getFavoriteMods(
            offset = _state.value.addons.size
        )
        result.onSuccess { mods ->
            _state.update {
                it.copy(
                    addons = it.addons + mods,
                    addonIsEndList = mods.isEmpty(),
                    likeScreenState = LikeScreenState.Success
                )
            }
        }.onFailure { error ->
            _state.update {
                it.copy(
                    likeScreenState = LikeScreenState.Error(
                        error.message ?: "Loading error"
                    )
                )
            }
        }

    }

    private fun getLikeTotalCount() = screenModelScope.launch {
        val size = repo.getLikeTotalSize()
        _state.update { it.copy(likeTotalCount = size) }
    }

}