package com.hamit.download

sealed interface DownloadEvent{
    data object ShowError: DownloadEvent
}
