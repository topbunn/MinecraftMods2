package com.hamit.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.hamit.ui.R

object AppFonts {

    val CORE = FontFamily(
        Font(R.font.regular, FontWeight.Normal),
        Font(R.font.medium, FontWeight.Medium),
        Font(R.font.semibold, FontWeight.SemiBold),
        Font(R.font.bold, FontWeight.Bold),
    )

}