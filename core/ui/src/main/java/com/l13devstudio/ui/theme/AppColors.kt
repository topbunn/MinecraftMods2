package com.l13devstudio.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.l13devstudio.domain.entity.appConfig.AppConfigProvider
import org.koin.mp.KoinPlatform.getKoin

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
    val shimmer: Color,
)

val LocalAppColors = staticCompositionLocalOf<LocalColors> {
    error("LocalColors not provided")
}

object AppColors {

    private val configProvider: AppConfigProvider
        get() = getKoin().get()

    val RED = Color(0xffEB4566)
    val GREEN = Color(0xff4EC565)

    val BG_LIGHT = Color(0xffF3F5FB)
    val BG_DARK = Color(0xff191B21)

    val CARD_LIGHT = Color(0xffffffff)
    val CARD_DARK = Color(0xff282A2F)

    val ON_PRIMARY_LIGHT = Color.White
    val ON_PRIMARY_DARK = Color(0xff191B21)

    val TITLE_LIGHT = Color(0xff262A33)
    val TITLE_DARK = Color(0xffEAEAEA)

    val TEXT_LIGHT = Color(0xff686A71)
    val TEXT_DARK = Color(0xffB3B5B7)

    val HINT_LIGHT = Color(0xffA1AAC2)
    val HINT_DARK = Color(0xff999B9F)

    val BORDER_LIGHT = Color(0xffF2F4FA)
    val BORDER_DARK = Color(0xff1C1E25)

    val SHIMMER_LIGHT = Color(0xffD9D9D9)
    val SHIMMER_DARK = Color(0xff686868)

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
    border = AppColors.BORDER_LIGHT,
    shimmer = AppColors.SHIMMER_LIGHT
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
    border = AppColors.BORDER_DARK,
    shimmer = AppColors.SHIMMER_DARK
)

