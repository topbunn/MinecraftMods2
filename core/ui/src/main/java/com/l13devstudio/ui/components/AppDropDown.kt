package com.l13devstudio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.l13devstudio.ui.theme.AppTypo
import com.l13devstudio.ui.theme.LocalAppColors
import com.l13devstudio.ui.utils.appDropShadow

@Composable
fun AppDropDown(
    selectedIndex: Int,
    items: List<Int>,
    modifier: Modifier = Modifier,
    onSelect: (index: Int) -> Unit,
) {
    val colors = LocalAppColors.current
    var mExpanded by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    Column {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .appDropShadow(RoundedCornerShape(10.dp), alpha = 0.15f, spread = 10.dp)
                .background(colors.card)
                .border(2.dp, colors.border, RoundedCornerShape(10.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(color = colors.primaryContainer)
                ){ mExpanded = !mExpanded }
                .padding(12.dp, 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(items[selectedIndex]),
                style = AppTypo.button,
                color = colors.text,
            )
            Icon(
                modifier = Modifier.rotate(if (mExpanded) 180f else 0f),
                painter = painterResource(com.l13devstudio.ui.R.drawable.ic_dropdown),
                contentDescription = "Choice type",
                tint = colors.primary
            )
        }

        DropdownMenu(
            containerColor = colors.background,
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(100.dp)
                .heightIn(max = 500.dp)
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(it),
                            style = AppTypo.button,
                            color = colors.text,
                        )
                    },
                    onClick = {
                        val index = items.indexOf(it).takeIf { it != -1 } ?: 0
                        onSelect(index)
                        mExpanded = false
                    }
                )
            }
        }
    }
}
