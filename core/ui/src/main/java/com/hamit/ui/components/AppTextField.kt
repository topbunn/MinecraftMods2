package com.hamit.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors

@Composable
fun AppTextField(
    text: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    hint: String = "",
    leadingIconRes: Int? = null,
    required: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    fontSize: TextUnit? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    tipContent: @Composable (() -> Unit)? = null,
    onTextChange: (String) -> Unit,
) {
    val colors = LocalAppColors.current

    val selectionColors = remember(colors.primary) {
        TextSelectionColors(
            handleColor = colors.primary,
            backgroundColor = colors.primary.copy(alpha = 0.2f)
        )
    }

    CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(2.dp, colors.border, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .padding(paddingValues),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top
            ) {

                leadingIconRes?.let {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(it),
                        contentDescription = null,
                        tint = colors.primary
                    )
                }

                BasicTextField(
                    modifier = Modifier.fillMaxSize(),
                    value = text,
                    onValueChange = onTextChange,
                    enabled = enabled,
                    readOnly = readOnly,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    cursorBrush = SolidColor(colors.primary),
                    textStyle = TextStyle(
                        fontFamily = AppTypo.placeholder.fontFamily,
                        fontWeight = AppTypo.placeholder.fontWeight,
                        fontSize = fontSize ?: AppTypo.placeholder.fontSize,
                        color = colors.text
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = if (singleLine)
                                Alignment.CenterStart
                            else
                                Alignment.TopStart
                        ) {

                            if (text.isEmpty()) {
                                Row {
                                    Text(
                                        text = hint,
                                        style = AppTypo.placeholder,
                                        color = colors.hint,
                                        fontSize = fontSize ?: AppTypo.placeholder.fontSize
                                    )
                                    if (required) {
                                        Text(
                                            text = "*",
                                            fontSize = 12.sp,
                                            color = AppColors.RED,
                                            modifier = Modifier.offset(y = (-4).dp)
                                        )
                                    }
                                }
                            }

                            innerTextField()
                        }
                    }
                )
            }

            tipContent?.let {
                Box(
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    it()
                }
            }
        }
    }
}
