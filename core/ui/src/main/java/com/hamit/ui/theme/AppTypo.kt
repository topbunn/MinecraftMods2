package com.hamit.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

object AppTypo {

    val H1 = TextStyle(
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        textAlign = TextAlign.Center
    )

    val H2 = TextStyle(
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )

    val H3 = TextStyle(
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
    )

    val M1 = TextStyle(
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )

    val placeholder = TextStyle(
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    )

    val button = TextStyle(
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    )

}