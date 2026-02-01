package com.l13devstudio.ui.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Stable
fun Modifier.appDropShadow(
    shape: Shape = RoundedCornerShape(32.dp),
    radius: Dp = 6.dp,
    spread: Dp = 0.dp,
    alpha: Float = 0.25f,
    offset: DpOffset = DpOffset(2.dp, 4.dp),

) = this.dropShadow(
    shape = shape,
    shadow = Shadow(
        radius = radius,
        spread = spread,
        alpha = alpha,
        offset = offset,
    )
)