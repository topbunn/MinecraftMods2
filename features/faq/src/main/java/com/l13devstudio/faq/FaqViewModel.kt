package com.l13devstudio.faq

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FaqViewModel: ScreenModel {

    private val _state = MutableStateFlow(FaqState())
    val state get() = _state.asStateFlow()

    fun openIssue(value: Boolean) = _state.update { it.copy(openIssue = value) }

}