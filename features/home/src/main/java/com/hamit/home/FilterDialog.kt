package com.hamit.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hamit.ui.components.BottomDialogWrapper
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors
import com.hamit.ui.utils.appDropShadow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FilterDialog(
    sortTypeItems: List<Int>,
    selectedSortTypeIndex: Int,
    onSelectSortType: (index: Int) -> Unit,

    addonTypeItems: List<Int>,
    selectedAddonTypeIndex: Int,
    onSelectAddonType: (index: Int) -> Unit,

    onDismissRequest: () -> Unit
) {
    BottomDialogWrapper(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SelectableRow(
                items = sortTypeItems,
                selectedIndex = selectedSortTypeIndex,
                onSelect = onSelectSortType
            )
            AppDropDown(
                modifier = Modifier.fillMaxWidth(),
                items = addonTypeItems,
                selectedIndex = selectedAddonTypeIndex,
                onSelect = onSelectAddonType
            )
        }
    }
}

@Composable
private fun AppDropDown(
    selectedIndex: Int,
    items: List<Int>,
    modifier: Modifier = Modifier,
    onSelect: (index: Int) -> Unit,
) {
    val colors = LocalAppColors.current
    var mExpanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .appDropShadow(RoundedCornerShape(10.dp), alpha = 0.15f, spread = 10.dp)
                .background(colors.card)
                .border(2.dp, colors.border, RoundedCornerShape(10.dp))
                .clickable { mExpanded = !mExpanded }
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
                painter = painterResource(com.hamit.ui.R.drawable.ic_dropdown),
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

@Composable
private fun SelectableRow(
    items: List<Int>,
    selectedIndex: Int,
    onSelect: (index: Int) -> Unit
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
