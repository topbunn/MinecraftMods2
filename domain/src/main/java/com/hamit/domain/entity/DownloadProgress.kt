package com.hamit.domain.entity

sealed interface DownloadFileStatus {

    data object Error: DownloadFileStatus

    data class Downloading(
        val bytesDownloaded: Long,
        val totalBytes: Long,
        val progress: Float
    ): DownloadFileStatus

    data object Success: DownloadFileStatus
}
