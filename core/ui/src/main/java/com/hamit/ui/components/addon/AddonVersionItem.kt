package com.hamit.ui.components.addon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.LocalAppColors

@Composable
fun AddonVersionItem(version: String) {
    val colors = LocalAppColors.current
    Text(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(colors.primaryContainer)
            .padding(horizontal = 10.dp, vertical = 2.dp),
        text = version,
        fontSize = 14.sp,
        color = colors.primary,
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.Bold,
    )
}