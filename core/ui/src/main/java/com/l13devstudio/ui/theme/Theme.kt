package com.l13devstudio.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val appColors = if (isDarkTheme) DarkThemeColors else LightThemeColors

    val customTextSelectionColors = TextSelectionColors(
        handleColor = AppColors.PRIMARY,
        backgroundColor = AppColors.PRIMARY.copy(0.2f)
    )

    CompositionLocalProvider(
        LocalAppColors provides appColors,
        LocalTextSelectionColors provides customTextSelectionColors
    ) {
        MaterialTheme(
            content = content
        )
    }
}