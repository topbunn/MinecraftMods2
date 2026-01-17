package com.hamit.download

data class AddonFileUi(
    val name: String,
    val link: String,
    val status: AddonFileUiStatus
){
    enum class AddonFileUiStatus{
        NO_SAVED, DOWNLOADING, SAVED
    }
}