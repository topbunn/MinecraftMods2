package com.l13devstudio.issue

internal data class IssueState(
    val email: String = "test@gmail.com",
    val desc: String = "",
    val isLoading: Boolean = false,
    val submitEnabled: Boolean = false
){

    internal enum class FieldType{
        EMAIL, DESC
    }

}
