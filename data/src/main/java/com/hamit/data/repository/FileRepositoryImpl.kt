package com.hamit.data.repository

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.hamit.domain.repository.FileRepository
import java.io.File

class FileRepositoryImpl(
    private val context: Context
): FileRepository {

    private val resolver = context.contentResolver

    override suspend fun exists(fileName: String): String? {
        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "mods"
        )
        val file = File(dir, fileName)
        return if (file.exists()) file.absolutePath else null
    }

    override suspend fun openFile(fileName: String) = runCatching {
        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "mods"
        )

        val file = File(dir, fileName)
        if (!file.exists() || !file.isFile) {
            throw IllegalStateException("File not found: $fileName")
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/octet-stream")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }

    override suspend fun createFile(fileName: String): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/java-archive")
                put(MediaStore.Downloads.RELATIVE_PATH, "Download/mods")
            }

            resolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                values
            ) ?: error("Failed to create file in MediaStore")
        } else {
            val dir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "mods"
            )
            if (!dir.exists()) dir.mkdirs()

            val file = File(dir, fileName)
            Uri.fromFile(file)
        }
    }

    override suspend fun deleteFile(fileName: String) {
        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "mods"
        )
        val file = File(dir, fileName)
        if (file.exists()) {
            file.delete()
        }
    }

}



