package com.hamit.suggest

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hamit.android.utills.isEmail
import com.hamit.domain.entity.suggest.SuggestEntity
import com.hamit.domain.useCases.suggest.SubmitSuggestUseCase
import com.hamit.suggest.SuggestState.FieldType.DESC
import com.hamit.suggest.SuggestState.FieldType.EMAIL
import com.hamit.suggest.SuggestState.FieldType.LINK
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

internal class SuggestViewModel(
    private val submitSuggestUseCase: SubmitSuggestUseCase
): ScreenModel {

    private val _state = MutableStateFlow(SuggestState())
    val state = _state.asStateFlow()

    private val _events = Channel<SuggestEvent>()
    val events get() = _events.receiveAsFlow()

    fun handleChangeState() {
        _state
            .map { listOf(it.email, it.link, it.desc) }
            .distinctUntilChanged()
            .drop(1)
            .onEach {
                _state.update { it.copy(submitEnabled = checkSubmitValid()) }
            }.launchIn(screenModelScope)
    }

    fun changeFieldValue(value: String, type: SuggestState.FieldType){
        _state.update {
            when(type){
                EMAIL -> if (value.length <= 64) it.copy(email = value) else it
                LINK -> if (value.length <= 256) it.copy(link = value) else it
                DESC -> if (value.length <= 2000) it.copy(desc = value) else it
            }
        }
    }


    fun submit() = screenModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val suggest = SuggestEntity(
            email = state.value.email,
            link = state.value.link,
            desc = state.value.desc
        )
        submitSuggestUseCase(suggest).onSuccess {
            _state.update { SuggestState() }
            _events.send(SuggestEvent.ShowSuccess)
        }.onFailure {
            _events.send(SuggestEvent.ShowError)
        }
        _state.update { it.copy(isLoading = false) }
    }

    private fun checkSubmitValid() = with(_state.value){
        email.isEmail() && link.isNotBlank() && desc.length >= 40
    }
}