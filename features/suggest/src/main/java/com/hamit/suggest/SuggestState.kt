package com.hamit.suggest

internal data class SuggestState(
    val email: String = "",
    val link: String = "",
    val desc: String = "",
    val isLoading: Boolean = false,
    val submitEnabled: Boolean = false
){

    enum class FieldType{
        EMAIL, LINK, DESC
    }

}
