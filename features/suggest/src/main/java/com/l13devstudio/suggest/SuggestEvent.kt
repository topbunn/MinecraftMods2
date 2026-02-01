package com.l13devstudio.suggest

sealed interface SuggestEvent{
    data object ShowSuccess: SuggestEvent
    data object ShowError: SuggestEvent
}
