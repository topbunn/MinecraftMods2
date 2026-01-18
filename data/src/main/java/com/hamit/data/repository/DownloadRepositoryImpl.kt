package com.hamit.data.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.hamit.data.source.remote.ApiFactory
import com.hamit.domain.entity.DownloadFileStatus
import com.hamit.domain.repository.DownloadRepository
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.cancel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class DownloadRepositoryImpl(
    private val context: Context,
    private val api: ApiFactory,
): DownloadRepository {

    override fun download(
        url: String,
        fileName: String
    ): Flow<DownloadFileStatus> = flow {
        try {
            val dir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "mods"
            )
            if (!dir.exists()) dir.mkdirs()

            val file = File(dir, fileName)

            val response = api.client.get(url)
            val channel = response.bodyAsChannel()

            val totalBytes = response.contentLength() ?: -1L
            var downloaded = 0L

            context.contentResolver
                .openOutputStream(Uri.fromFile(file))!!
                .use { output ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

                    try {
                        while (!channel.isClosedForRead) {
                            val read = channel.readAvailable(buffer)
                            if (read <= 0) break

                            output.write(buffer, 0, read)
                            downloaded += read

                            emit(
                                DownloadFileStatus.Downloading(
                                    bytesDownloaded = downloaded,
                                    totalBytes = totalBytes,
                                    progress = if (totalBytes > 0)
                                        downloaded.toFloat() / totalBytes
                                    else 0f
                                )
                            )
                        }
                        output.flush()
                        emit(DownloadFileStatus.Success)
                    } finally {
                        channel.cancel()
                    }
                }
        } catch (e: Exception){
            e.printStackTrace()
            emit(DownloadFileStatus.Error)
        }
    }.flowOn(Dispatchers.IO)

}