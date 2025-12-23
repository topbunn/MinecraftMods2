package ru.topbun.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import ru.topbun.ui.R

object AppFonts {

    private fun createFont(fontRes: Int) = FontFamily(Font(fontRes))

    object SF {

        val BOLD = createFont(R.font.bold)
        val SEMI_BOLD = createFont(R.font.semibold)
        val REGULAR = createFont(R.font.regular)
        val MEDIUM = createFont(R.font.medium)

    }

}