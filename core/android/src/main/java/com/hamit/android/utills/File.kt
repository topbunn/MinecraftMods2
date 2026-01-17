package com.hamit.android.utills

import android.os.Environment
import java.io.File

fun getAddonFile(fileName: String): File? {
    val downloadsDir = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "mods"
    )
    val targetFile = File(downloadsDir, fileName)
    return if (targetFile.exists() && targetFile.isFile) targetFile else null
}


fun String.getModNameFromUrl(type: String = ""): String{
    val isPrimitiveLink = this.takeLast(10).contains('.')
    return if (isPrimitiveLink) {
        substringAfterLast('/')
    } else {
        substringBeforeLast("/?")
            .substringAfterLast('/')
            .substringBeforeLast('.') + type
    }.replace("%20", " ")
}