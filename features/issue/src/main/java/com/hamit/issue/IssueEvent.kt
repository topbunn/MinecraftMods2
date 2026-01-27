package com.hamit.issue

sealed interface IssueEvent{
    data object ShowSuccess: IssueEvent
    data object ShowError: IssueEvent
}
