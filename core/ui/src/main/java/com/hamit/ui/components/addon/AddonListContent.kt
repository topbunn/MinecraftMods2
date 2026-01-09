package com.hamit.ui.components.addon

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.hamit.domain.entity.addon.AddonEntity

internal fun LazyListScope.AddonListContent(
    addons: List<AddonEntity>,
    isLoad: Boolean
) {
    if (addons.isEmpty() && isLoad) {
        items(3) {
            AddonShimmer()
        }
    } else {
        items(
            items = constructList(addons),
            key = { it.hashCode() }
        ) { item ->
            when (item) {
                is AddonListItemType.Addon -> {
                    AddonItem(addon = item.addon)
                }

                else -> {}
            }
        }
    }
}
