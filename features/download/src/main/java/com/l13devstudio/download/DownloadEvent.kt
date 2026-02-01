package com.l13devstudio.download

sealed interface DownloadEvent{
    data object ShowDownloadError: DownloadEvent
    data object ShowInstallError: DownloadEvent
}
