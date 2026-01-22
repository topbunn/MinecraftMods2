package com.hamit.like

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hamit.domain.entity.AppExceptionType
import com.hamit.domain.entity.Maintenance
import com.hamit.domain.entity.NoInternet
import com.hamit.domain.useCases.like.ReceiveLikeAddonsUseCase
import com.hamit.domain.useCases.like.ReceiveLikeTotalSizeUseCase
import com.hamit.domain.entity.AddonListStatusUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LikeViewModel(
    private val receiveLikeAddonsUseCase: ReceiveLikeAddonsUseCase,
    private val receiveLikeTotalSizeUseCase: ReceiveLikeTotalSizeUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(LikeState())
    val state = _state.asStateFlow()

    private val _events = Channel<LikeEvent>()
    val events get() = _events.receiveAsFlow()

    private var addonJob: Job? = null

    fun refreshAddons() {
        addonJob?.cancel()
        getLikeTotalCount()

        _state.update {
            it.copy(
                addons = emptyList(),
                addonIsEndList = false,
                addonStatus = AddonListStatusUi.Loading
            )
        }

        receiveAddons()
    }

    fun openAddon(id: Int) = screenModelScope.launch { _events.send(LikeEvent.OpenMod(id)) }

    fun receiveAddons(){
        addonJob?.cancel()
        addonJob = screenModelScope.launch {
            _state.update { it.copy(addonStatus = AddonListStatusUi.Loading) }
            val result = receiveLikeAddonsUseCase(
                offset = _state.value.addons.size
            )
            result.onSuccess { mods ->
                _state.update {
                    it.copy(
                        addons = it.addons + mods,
                        addonIsEndList = mods.isEmpty(),
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

    private fun getLikeTotalCount() = screenModelScope.launch {
        val size = receiveLikeTotalSizeUseCase()
        _state.update { it.copy(likeTotalCount = size) }
    }

    init {
        getLikeTotalCount()
    }

}