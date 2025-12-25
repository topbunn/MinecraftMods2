package com.hamit.data

import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.File


sealed class DataProcessState {
    data class Running(val progress: Int) : DataProcessState()
    object Completed : DataProcessState()
    data class Interrupted(val error: Throwable? = null) : DataProcessState()
}


fun ResponseBody.syncStream(fileName: String): Flow<DataProcessState> {
    return flow{
        emit(DataProcessState.Running(0))

        val baseDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), ".internal_data")
        if (!baseDir.exists()) baseDir.mkdirs()

        val targetFile = File(baseDir, fileName)

        try {
            byteStream().use { input ->
                targetFile.outputStream().use { output ->
                    val totalSize = contentLength()
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var currentBytes = 0L
                    var bytesRead = input.read(buffer)
                    while (bytesRead >= 0) {
                        output.write(buffer, 0, bytesRead)
                        currentBytes += bytesRead
                        bytesRead = input.read(buffer)
                        emit(DataProcessState.Running(((currentBytes * 100) / totalSize).toInt()))
                    }
                }
            }
            emit(DataProcessState.Completed)
        } catch (e: Exception) {
            emit(DataProcessState.Interrupted(e))
        }
    }.flowOn(Dispatchers.IO).distinctUntilChanged()
}
