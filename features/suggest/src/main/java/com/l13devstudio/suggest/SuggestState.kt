package com.l13devstudio.suggest

internal data class SuggestState(
    val email: String = "",
    val desc: String = "",
    val isLoading: Boolean = false,
    val submitEnabled: Boolean = false
){

    enum class FieldType{
        EMAIL, DESC
    }

}
