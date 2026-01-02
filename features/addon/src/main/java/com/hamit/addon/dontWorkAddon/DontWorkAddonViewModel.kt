package com.hamit.addon.dontWorkAddon

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hamit.addon.dontWorkAddon.DontWorkAddonState.DontWorkScreenState
import com.hamit.android.utills.isEmail
import com.hamit.domain.entity.problem.ProblemEntity
import com.hamit.domain.useCases.problem.SendProblemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DontWorkAddonViewModel(
    private val sendProblemUseCase: SendProblemUseCase,
) : ScreenModel {

    private val _state = MutableStateFlow(DontWorkAddonState())
    val state = _state.asStateFlow()

    fun changeEmail(email: String) = _state.update { it.copy(email = email) }
    fun changeMessage(message: String) = _state.update { it.copy(message = message) }

    fun sendIssue() = screenModelScope.launch {
        _state.update { it.copy(feedbackState = DontWorkScreenState.Loading) }
        val problem = ProblemEntity(email = state.value.email, text = state.value.message)
        val result = sendProblemUseCase(problem)
        result.onSuccess {
            _state.update {
                it.copy(
                    feedbackState = DontWorkScreenState.Success,
                    email = "",
                    message = ""
                )
            }
        }.onFailure {
            _state.update { it.copy(feedbackState = DontWorkScreenState.Error(it.message)) }
        }
    }

    val buttonEnabled = _state.map {
        it.email.isEmail() && it.message.length in (32..1024)
    }.stateIn(screenModelScope, SharingStarted.Eagerly, false)

}