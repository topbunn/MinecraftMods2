package com.l13devstudio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.l13devstudio.ui.theme.AppTypo
import com.l13devstudio.ui.theme.LocalAppColors
import com.l13devstudio.ui.utils.appDropShadow

@Composable
fun SelectableRow(
    items: List<Int>,
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onSelect: (index: Int) -> Unit
) {
    val colors = LocalAppColors.current
    Row(
        modifier = modifier
            .appDropShadow(RoundedCornerShape(10.dp), alpha = 0.15f)
            .background(colors.background, RoundedCornerShape(10.dp))
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            SelectableItem(
                text = stringResource(item),
                isSelected = isSelected,
                modifier = Modifier.weight(1f)
            ){
                onSelect(index)
            }
        }
    }
}

@Composable
private fun SelectableItem(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val colors = LocalAppColors.current
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background( if(isSelected) colors.card else Color.Transparent )
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 10.dp),
        text = text,
        style = AppTypo.button,
        textAlign = TextAlign.Center,
        color = if(isSelected) colors.primary else colors.text,
    )
}