package com.hamit.download

sealed interface DownloadEvent{
    data object ShowDownloadError: DownloadEvent
    data object ShowInstallError: DownloadEvent
}
