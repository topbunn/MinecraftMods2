package com.hamit.ui.components.addon

import com.hamit.android.AppExceptionType

sealed interface AddonListStatusUi {

    object Idle: AddonListStatusUi
    object Success: AddonListStatusUi
    object Loading: AddonListStatusUi
    class Error(val type: AppExceptionType): AddonListStatusUi

}