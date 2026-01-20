package com.hamit.data.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.hamit.data.source.remote.ApiFactory
import com.hamit.domain.entity.DownloadFileStatus
import com.hamit.domain.repository.DownloadRepository
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext

class DownloadRepositoryImpl(
    private val context: Context,
    private val api: ApiFactory,
): DownloadRepository {

    override fun download(
        url: String,
        fileName: String
    ): Flow<DownloadFileStatus> = flow {

        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "mods"
        )
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, fileName)

        try {
            api.client.prepareGet(url).execute { response ->

                val totalBytes = response.contentLength() ?: -1L
                val channel = response.bodyAsChannel()

                var downloaded = 0L
                val buffer = ByteArray(8 * 1024)

                context.contentResolver
                    .openOutputStream(Uri.fromFile(file))!!
                    .use { output ->

                        while (true) {
                            coroutineContext.ensureActive()

                            val bytesRead = channel.readAvailable(buffer)
                            if (bytesRead == -1) break

                            output.write(buffer, 0, bytesRead)
                            downloaded += bytesRead

                            emit(
                                DownloadFileStatus.Downloading(
                                    bytesDownloaded = downloaded,
                                    totalBytes = totalBytes,
                                    progress = if (totalBytes > 0)
                                        downloaded.toFloat() / totalBytes
                                    else -1f
                                )
                            )
                        }
                    }

                emit(DownloadFileStatus.Success)
            }

        } catch (e: CancellationException) {
            file.delete()
            throw e
        } catch (e: Exception) {
            file.delete()
            emit(DownloadFileStatus.Error)
        }
    }.flowOn(Dispatchers.IO)



}