package com.hamit.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hamit.ui.components.AppDropDown
import com.hamit.ui.components.BottomDialogWrapper
import com.hamit.ui.components.SelectableRow

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
                modifier = Modifier.fillMaxWidth(),
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


