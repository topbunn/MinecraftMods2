package com.hamit.addon.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.R
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.LocalAppColors

@Composable
internal fun Description(addon: AddonEntity, isExpanded: Boolean, onTextExpand: () -> Unit) {
    val colors = LocalAppColors.current
    Text(
        modifier = Modifier.padding(horizontal = 20.dp),
        text = stringResource(R.string.hint_desc),
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = colors.title,
    )
    Spacer(Modifier.height(10.dp))
    Text(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .clickable { onTextExpand() },
        text = addon.desc,
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        maxLines = if (isExpanded) Int.MAX_VALUE else 6,
        overflow = TextOverflow.Ellipsis,
        color = colors.text,
    )
}
