package com.hamit.home

import cafe.adriel.voyager.core.model.ScreenModel
import com.hamit.domain.useCases.addon.ReceiveAddonListUseCase
import com.hamit.domain.useCases.like.AddLikeUseCase
import com.hamit.home.HomeState.FilterType.ADDON_TYPES
import com.hamit.home.HomeState.FilterType.SORTS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class HomeViewModel(
    private val addLikeUseCase: AddLikeUseCase,
    private val receiveAddonListUseCase: ReceiveAddonListUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun changeFilterValue(selectedIndex: Int, type: HomeState.FilterType){
        _state.update {
            when(type){
                SORTS -> it.copy(selectedSortTypeIndex = selectedIndex)
                ADDON_TYPES -> it.copy(selectedAddonTypeIndex = selectedIndex)
            }
        }
    }

    fun changeFilterDialog(status: Boolean) = _state.update { it.copy(filterIsOpen = status) }

}