package com.hamit.android.utills

import android.os.Environment
import java.io.File

fun getModFile(fileName: String): File? {
    val downloadsDir = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "mods"
    )
    val targetFile = File(downloadsDir, fileName)
    return if (targetFile.exists() && targetFile.isFile) targetFile else null
}


fun String.getModNameFromUrl(type: String = "") = substringBeforeLast("/?")
    .substringAfterLast('/')
    .substringBeforeLast('.')
    .replace("%20", " ")
    .replace("%2", "")
    .replace("%", "") + type
