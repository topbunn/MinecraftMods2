package com.hamit.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppTypo

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    foregroundColor: Color = AppColors.WHITE,
    label: String,
    onAction: () -> Unit
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor.copy(0.5f)
        ),
        enabled = isEnabled || isLoading,
        shape = RoundedCornerShape(8.dp),
        onClick = {
            onAction()
        }
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = AppColors.WHITE,
                strokeWidth = 2.5.dp,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = label,
                style = AppTypo.APP_TEXT,
                fontSize = 16.sp,
                color = foregroundColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
