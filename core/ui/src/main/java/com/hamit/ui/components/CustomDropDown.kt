package com.hamit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo

@Composable
fun CustomDropDown(
    selectedValue: String,
    options: List<String>,
    modifier: Modifier = Modifier,
    onSelectionChanged: (String) -> Unit,
) {

    var isExpanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = modifier.clickable { isExpanded = !isExpanded },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedValue,
                style = AppTypo.APP_TEXT,
                fontSize = 15.sp,
                color = AppColors.GRAY,
                fontFamily = AppFonts.CORE.MEDIUM
            )

            Icon(
                modifier = Modifier.rotate(if (isExpanded) 180f else 0f),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Choice type",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            containerColor = AppColors.GRAY_BG,
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .width(100.dp)
                .heightIn(max = 500.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = AppTypo.APP_TEXT,
                            fontSize = 14.sp,
                            color = AppColors.GRAY,
                            fontFamily = AppFonts.CORE.MEDIUM
                        )
                    },
                    onClick = {
                        onSelectionChanged(option)
                        isExpanded = false
                    }
                )
            }
        }
    }
}
