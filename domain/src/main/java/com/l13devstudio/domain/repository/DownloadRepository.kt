package com.l13devstudio.domain.repository

import com.l13devstudio.domain.entity.DownloadFileStatus
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {

    fun download(
        url: String,
        fileName: String
    ): Flow<DownloadFileStatus>

}