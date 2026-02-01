package com.l13devstudio.download

data class AddonFileUi(
    val name: String,
    val link: String,
    val status: AddonFileUiStatus
){
    sealed interface AddonFileUiStatus{

        object NoSaved: AddonFileUiStatus

        data class Downloading(
            val bytesDownloaded: Long,
            val totalBytes: Long,
            val progress: Float
        ): AddonFileUiStatus

        data class Saved(val path: String): AddonFileUiStatus
    }
}