package com.hamit.addon.issue

sealed interface IssueEvent{
    data object ShowSuccess: IssueEvent
    data object ShowError: IssueEvent
}
