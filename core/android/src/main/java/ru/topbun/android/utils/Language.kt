package ru.topbun.android.utils

import java.util.Locale


fun receiveLanguageFromDevice(): String {
    val lang = Locale.getDefault().language
    val supLanguage = setOf("ru", "de", "es", "fr", "hi", "it", "ja", "ko", "pt", "ar", "en")
    return if (supLanguage.contains(lang)) lang else "en"
}