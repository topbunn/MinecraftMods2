package com.hamit.domain.entity

sealed interface AddonListStatusUi {

    object Idle: AddonListStatusUi
    object Success: AddonListStatusUi
    object Loading: AddonListStatusUi
    class Error(val type: AppExceptionType): AddonListStatusUi

}