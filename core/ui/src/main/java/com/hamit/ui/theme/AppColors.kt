package com.hamit.ui.theme

import androidx.compose.ui.graphics.Color
import org.koin.mp.KoinPlatform.getKoin
import com.hamit.domain.entity.appConfig.AppConfigProvider

object AppColors {

    private val configProvider: AppConfigProvider
        get() = getKoin().get()

    val WHITE = Color(0xFFFFFFFF)
    val GRAY = Color(0xFFDFDFDF)
    val BUTTON_RED = Color(0xFFFF1A1A)

    val BLACK_BG = Color(0xFF1B1B1B)
    val GRAY_BG = Color(0xFF2A2A2A)

    val PRIMARY = Color(configProvider.getConfig().primaryColor)
}