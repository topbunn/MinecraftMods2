package com.l13devstudio.issue

sealed interface IssueEvent{
    data object ShowSuccess: IssueEvent
    data object ShowError: IssueEvent
}
