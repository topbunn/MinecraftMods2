package com.hamit.addon.issue

import android.system.Os.link
import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.google.common.net.HttpHeaders.LINK
import com.hamit.addon.issue.IssueState.FieldType.*
import com.hamit.android.utills.isEmail
import com.hamit.domain.entity.problem.ProblemEntity
import com.hamit.domain.entity.suggest.SuggestEntity
import com.hamit.domain.useCases.problem.SendProblemUseCase
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
                EMAIL -> if (text.length <= 64) it.copy(email = text) else it
                DESC -> if (text.length <= 2000) it.copy(desc = text) else it
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