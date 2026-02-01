package com.l13devstudio.suggest

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.l13devstudio.android.utills.isEmail
import com.l13devstudio.domain.entity.suggest.SuggestEntity
import com.l13devstudio.domain.useCases.suggest.SubmitSuggestUseCase
import com.l13devstudio.suggest.SuggestState.FieldType.DESC
import com.l13devstudio.suggest.SuggestState.FieldType.EMAIL
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
) : ScreenModel {

    private val _state = MutableStateFlow(SuggestState())
    val state = _state.asStateFlow()

    private val _events = Channel<SuggestEvent>()
    val events get() = _events.receiveAsFlow()

    fun handleChangeState() {
        _state
            .map { listOf(it.email, it.desc) }
            .distinctUntilChanged()
            .drop(1)
            .onEach {
                _state.update { it.copy(submitEnabled = checkSubmitValid()) }
            }.launchIn(screenModelScope)
    }

    fun changeFieldValue(value: String, type: SuggestState.FieldType) {
        _state.update {
            when (type) {
                EMAIL -> if (value.length <= 64) it.copy(email = value) else it
                DESC -> if (value.length <= 2000) it.copy(desc = value) else it
            }
        }
    }


    fun submit() = screenModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val suggest = SuggestEntity(
            email = state.value.email,
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

    private fun checkSubmitValid() = with(_state.value) {
        email.isEmail() && desc.length >= 40
    }
}