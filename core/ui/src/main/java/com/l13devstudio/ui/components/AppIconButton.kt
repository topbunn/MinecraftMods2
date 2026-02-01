package com.l13devstudio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.l13devstudio.ui.theme.LocalAppColors

@Composable
fun AppIconButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(0.45f))
            .clickable{ onClick() }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ){
        Icon(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = null,
            tint = colors.primary
        )
    }
}