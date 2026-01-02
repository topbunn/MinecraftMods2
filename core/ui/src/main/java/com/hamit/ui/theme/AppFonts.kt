package com.hamit.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.hamit.ui.R

object AppFonts {

    private fun createFont(fontRes: Int) = FontFamily(Font(fontRes))

    object CORE {

        val BOLD = createFont(R.font.bold)
        val SEMI_BOLD = createFont(R.font.semibold)
        val REGULAR = createFont(R.font.regular)
        val MEDIUM = createFont(R.font.medium)

    }

}