package com.hamit.addon.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamit.android.utills.getModNameFromUrl
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.R
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.LocalAppColors

@Composable
internal fun Files(addon: AddonEntity, onClick: () -> Unit) {
    val colors = LocalAppColors.current
    Text(
        modifier = Modifier.padding(horizontal = 20.dp),
        text = stringResource(R.string.files),
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = colors.title,
    )
    Spacer(Modifier.height(10.dp))
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        addon.files.forEach {
            FileButton(it.getModNameFromUrl(addon.type.toExtension())) { onClick() }
        }
    }
}

@Composable
private fun FileButton(
    text: String,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current
    Text(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colors.primaryContainer)
            .clickable{ onClick() }
            .padding(15.dp),
        text = text,
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = colors.primary,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.MiddleEllipsis
    )
}
