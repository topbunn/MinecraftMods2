package com.hamit.domain.repository

import android.net.Uri

interface FileRepository {
    suspend fun exists(fileName: String): String?
    suspend fun openFile(fileName: String): Result<Unit>
    suspend fun createFile(fileName: String): Uri
    suspend fun deleteFile(fileName: String)
}