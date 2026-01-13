package com.hamit.addon.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.R
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors

@Composable
internal fun SupportButtons(
    onClickHowInstall: () -> Unit,
    onClickNotWork: () -> Unit,
) {
    val colors = LocalAppColors.current
    Text(
        modifier = Modifier.padding(horizontal = 20.dp),
        text = stringResource(R.string.support),
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
        Button(
            text = stringResource(R.string.how_to_install),
            icon = painterResource(R.drawable.ic_how_install),
            color = AppColors.GREEN,
            onClick = onClickHowInstall
        )

        Button(
            text = stringResource(R.string.not_working),
            icon = painterResource(R.drawable.ic_not_work),
            color = AppColors.RED,
            onClick = onClickNotWork
        )
    }
}

@Composable
private fun Button(text: String, icon: Painter,  color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color = color)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = icon,
                contentDescription = text,
                tint = Color.White
            )
            Text(
                text = text,
                style = AppTypo.button,
                color = Color.White
            )
        }
    }
}