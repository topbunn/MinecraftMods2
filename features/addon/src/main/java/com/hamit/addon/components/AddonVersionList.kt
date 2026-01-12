package com.hamit.addon.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.components.addon.AddonVersionItem

@Composable
internal fun AddonVersionList(addon: AddonEntity) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        addon.versions.forEach {
            AddonVersionItem(it)
        }
    }
}
