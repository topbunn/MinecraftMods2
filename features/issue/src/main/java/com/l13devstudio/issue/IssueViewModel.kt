package com.l13devstudio.issue

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.l13devstudio.android.utills.isEmail
import com.l13devstudio.domain.entity.problem.ProblemEntity
import com.l13devstudio.domain.useCases.problem.SendProblemUseCase
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

internal class IssueViewModel(
    private val sendProblemUseCase: SendProblemUseCase
): ScreenModel {

    private val _state = MutableStateFlow(IssueState())
    val state = _state.asStateFlow()


    private val _events = Channel<IssueEvent>()
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

    fun changeFieldValue(text: String, type: IssueState.FieldType) {
        _state.update {
            when(type){
                IssueState.FieldType.EMAIL -> if (text.length <= 64) it.copy(email = text) else it
                IssueState.FieldType.DESC -> if (text.length <= 2000) it.copy(desc = text) else it
            }
        }
    }

    fun submit() = screenModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val problem = ProblemEntity(
            email = state.value.email,
            text = state.value.desc
        )
        sendProblemUseCase(problem).onSuccess {
            _state.update { IssueState() }
            _events.send(IssueEvent.ShowSuccess)
        }.onFailure {
            _events.send(IssueEvent.ShowError)
        }
        _state.update { it.copy(isLoading = false) }
    }

    private fun checkSubmitValid() = with(_state.value){
        email.isEmail() && desc.length >= 40
    }

}