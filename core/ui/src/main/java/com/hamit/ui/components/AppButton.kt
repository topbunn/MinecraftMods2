package com.hamit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    container: Color? = null,
    content: Color? = null,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    shape: Shape = RoundedCornerShape(10.dp),
    afterTextContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    val colors = LocalAppColors.current
    val containerColor = container ?: colors.primary
    val contentColor = content ?: colors.onPrimary
    val interaction = MutableInteractionSource()
    val isEnabled = !isLoading && isEnabled
    Box(
        modifier = modifier
            .clip(shape)
            .alpha(if (!isEnabled) 0.5f else 1f)
            .background(containerColor)
            .clickable(
                interactionSource = interaction,
                enabled = isEnabled,
                indication = ripple(color = contentColor)
            ) {
                if (isEnabled) {
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = contentColor,
                modifier = Modifier.size(18.dp),
                strokeWidth = 3.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    style = AppTypo.button,
                    color = contentColor
                )
                afterTextContent?.let { content ->
                    content()
                }
            }

        }
    }
}