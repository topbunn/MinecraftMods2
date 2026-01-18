package com.hamit.domain.repository

import com.hamit.domain.entity.DownloadFileStatus
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {

    fun download(
        url: String,
        fileName: String
    ): Flow<DownloadFileStatus>

}