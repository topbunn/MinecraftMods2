package com.hamit.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import org.koin.mp.KoinPlatform.getKoin
import com.hamit.domain.entity.appConfig.AppConfigProvider

@Immutable
data class LocalColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val background: Color,
    val card: Color,
    val title: Color,
    val text: Color,
    val hint: Color,
    val border: Color,
)

val LocalAppColors = staticCompositionLocalOf<LocalColors> {
    error("LocalColors not provided")
}

object AppColors {

    private val configProvider: AppConfigProvider
        get() = getKoin().get()

    val BG_LIGHT = Color(0xffF3F5FB)
    val BG_DARK = Color(0xff191B21)

    val CARD_LIGHT = Color(0xffffffff)
    val CARD_DARK = Color(0xff282A2F)

    val ON_PRIMARY_LIGHT = Color.White
    val ON_PRIMARY_DARK = Color(0xff191B21)

    val TITLE_LIGHT = Color(0xff262A33)
    val TITLE_DARK = Color(0xffEAEAEA)

    val TEXT_LIGHT = Color(0xff262A33).copy(0.7f)
    val TEXT_DARK = Color(0xffB3B5B7)

    val HINT_LIGHT = Color(0xffA1AAC2)
    val HINT_DARK = Color(0xff999B9F)

    val BORDER_LIGHT = Color(0xffF2F4FA)
    val BORDER_DARK = Color(0xff1C1E25)

    val PRIMARY = Color(configProvider.getConfig().primaryColor)
}

val LightThemeColors = LocalColors(
    primary = AppColors.PRIMARY,
    onPrimary = AppColors.ON_PRIMARY_LIGHT,
    primaryContainer = AppColors.PRIMARY.copy(0.2f),
    background = AppColors.BG_LIGHT,
    card = AppColors.CARD_LIGHT,
    title = AppColors.TITLE_LIGHT,
    text = AppColors.TEXT_LIGHT,
    hint = AppColors.HINT_LIGHT,
    border = AppColors.BORDER_LIGHT
)

val DarkThemeColors = LocalColors(
    primary = AppColors.PRIMARY,
    onPrimary = AppColors.ON_PRIMARY_DARK,
    primaryContainer = AppColors.PRIMARY.copy(0.2f),
    background = AppColors.BG_DARK,
    card = AppColors.CARD_DARK,
    title = AppColors.TITLE_DARK,
    text = AppColors.TEXT_DARK,
    hint = AppColors.HINT_DARK,
    border = AppColors.BORDER_DARK
)

