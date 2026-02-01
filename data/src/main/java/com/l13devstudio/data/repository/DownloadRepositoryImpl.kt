package com.l13devstudio.data.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.l13devstudio.data.source.remote.ApiFactory
import com.l13devstudio.domain.entity.DownloadFileStatus
import com.l13devstudio.domain.repository.DownloadRepository
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import kotlin.coroutines.cancellation.CancellationException

class DownloadRepositoryImpl(
    private val context: Context,
    private val api: ApiFactory,
): DownloadRepository {

    override fun download(
        url: String,
        fileName: String
    ): Flow<DownloadFileStatus> = channelFlow {

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
                            val bytesRead = channel.readAvailable(buffer)
                            if (bytesRead == -1) break

                            output.write(buffer, 0, bytesRead)
                            downloaded += bytesRead

                            send(
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
            }
            send(DownloadFileStatus.Success)
        } catch (e: Exception) {
            if (e is CancellationException) {
                file.delete()
                throw e
            }
            file.delete()
            send(DownloadFileStatus.Error)
        }
    }.flowOn(Dispatchers.IO)
}