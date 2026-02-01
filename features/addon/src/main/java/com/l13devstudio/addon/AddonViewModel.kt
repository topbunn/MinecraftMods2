package com.l13devstudio.addon

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.l13devstudio.domain.entity.AddonListStatusUi
import com.l13devstudio.domain.entity.AppExceptionType
import com.l13devstudio.domain.entity.Maintenance
import com.l13devstudio.domain.entity.NoInternet
import com.l13devstudio.domain.entity.addon.AddonSortType
import com.l13devstudio.domain.entity.like.LikeEntity
import com.l13devstudio.domain.useCases.addon.ReceiveAddonListUseCase
import com.l13devstudio.domain.useCases.addon.ReceiveAddonUseCase
import com.l13devstudio.domain.useCases.like.AddLikeUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddonViewModel(
    private val addonId: Int,
    private val receiveAddonUseCase: ReceiveAddonUseCase,
    private val receiveAddonListUseCase: ReceiveAddonListUseCase,
    private val addLikeUseCase: AddLikeUseCase,
) : ScreenModel {

    private val _state = MutableStateFlow(AddonState())
    val state get() = _state.asStateFlow()

    private var job: Job? = null

    init {
        loadAddon()
        loadOtherAddons()
    }

    fun loadAddon() {
        job?.cancel()
        job = screenModelScope.launch {
            _state.update { it.copy(addon = null, loadStatus = AddonListStatusUi.Loading) }
            val result = receiveAddonUseCase(addonId)
            result.onSuccess { addon ->
                _state.update { it.copy(addon = addon, loadStatus = AddonListStatusUi.Success) }
            }.onFailure { error ->

                error.printStackTrace()

                if (error is java.util.concurrent.CancellationException) { return@launch }
                val type = when (error) {
                    NoInternet -> AppExceptionType.NoInternet
                    Maintenance -> AppExceptionType.Maintenance
                    else -> AppExceptionType.Error
                }
                _state.update { it.copy(loadStatus = AddonListStatusUi.Error(type)) }
            }

        }
    }

    fun switchTextExpand() = _state.update {
        it.copy(textIsExpand = !state.value.textIsExpand)
    }

    fun openIssue(value: Boolean) = _state.update { it.copy(openProblem = value) }

    fun switchLikeStatus() = screenModelScope.launch {
        state.value.addon?.let { mod ->
            val newLike = LikeEntity(
                addonId = mod.id,
                isActive = !mod.isLike
            )
            addLikeUseCase(newLike)
            val newAddon = mod.copy(isLike = newLike.isActive)
            _state.update { it.copy(addon = newAddon) }
        }
    }

    private fun loadOtherAddons() = screenModelScope.launch{
        val result = receiveAddonListUseCase(
            q = "",
            offset = 0,
            type = null,
            sortType = AddonSortType.RELEVANCE,
            limit = 4
        )
        result.onSuccess { addons ->
            val addons = addons.filter { it.id != addonId }.take(3)
            _state.update { it.copy(otherAddons = addons) }
        }.onFailure {
            it.printStackTrace()
        }
    }

}