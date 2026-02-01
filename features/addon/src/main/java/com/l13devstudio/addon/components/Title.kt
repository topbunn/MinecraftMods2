package com.l13devstudio.addon.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.l13devstudio.domain.entity.addon.AddonEntity
import com.l13devstudio.ui.theme.AppTypo
import com.l13devstudio.ui.theme.LocalAppColors

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