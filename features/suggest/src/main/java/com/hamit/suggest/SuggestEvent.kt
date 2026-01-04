package com.hamit.suggest

sealed interface SuggestEvent{
    data object ShowSuccess: SuggestEvent
    data object ShowError: SuggestEvent
}
