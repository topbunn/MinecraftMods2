package com.hamit.issue

internal data class IssueState(
    val email: String = "",
    val desc: String = "",
    val isLoading: Boolean = false,
    val submitEnabled: Boolean = false
){

    internal enum class FieldType{
        EMAIL, DESC
    }

}
