package com.hamit.addon.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors

@Composable
internal fun Title(addon: AddonEntity) {
    val colors = LocalAppColors.current
    Text(
        modifier = Modifier.padding(horizontal = 20.dp),
        text = addon.name,
        style = AppTypo.H2,
        color = colors.title,
        fontWeight = FontWeight.SemiBold
    )
}