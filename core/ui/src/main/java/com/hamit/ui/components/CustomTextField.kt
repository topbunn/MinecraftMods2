package com.hamit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo

@Composable
fun CustomTextField(
    textValue: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    leadingIcon: (@Composable () -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
    isEnabled: Boolean = true,
    imeOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    imeActions: KeyboardActions = KeyboardActions.Default,
    transformation: VisualTransformation = VisualTransformation.None,
    isSingleLine: Boolean = true,
    backgroundColor: Color = AppColors.GRAY_BG,
    textAlignment: Alignment = Alignment.CenterStart,
    maxLineCount: Int = if (isSingleLine) 1 else Int.MAX_VALUE,
    minLineCount: Int = 1,
) {
    Row(
        modifier = modifier.background(backgroundColor, RoundedCornerShape(6.dp)),
        verticalAlignment = verticalAlignment
    ) {
        leadingIcon?.let { icon ->
            Spacer(modifier = Modifier.width(12.dp))
            icon()
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(contentPadding),
            contentAlignment = textAlignment
        ) {
            if (textValue.isEmpty()) {
                Text(
                    text = hint,
                    style = AppTypo.APP_TEXT,
                    fontSize = 16.sp,
                    fontFamily = AppFonts.CORE.MEDIUM,
                    color = AppColors.GRAY.copy(0.7f)
                )
            }

            val fieldTextStyle = TextStyle(
                color = AppColors.GRAY,
                fontSize = 16.sp,
                fontFamily = AppFonts.CORE.MEDIUM
            )

            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = textValue,
                onValueChange = onTextChanged,
                enabled = isEnabled,
                textStyle = fieldTextStyle,
                keyboardOptions = imeOptions,
                keyboardActions = imeActions,
                singleLine = isSingleLine,
                maxLines = maxLineCount,
                minLines = minLineCount,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                visualTransformation = transformation
            )
        }
    }
}
