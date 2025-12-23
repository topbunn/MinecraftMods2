package com.hamit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hamit.android.ad.natives.NativeAdCoordinator
import com.hamit.domain.entity.mod.ModEntity

sealed class AddonsListItem {
    data class AddonItem(val mod: ModEntity) : AddonsListItem()
    object AdItem : AddonsListItem()
}

fun buildAddonList(addons: List<ModEntity>): List<AddonsListItem> {
    val result = ArrayList<AddonsListItem>()
    addons.forEachIndexed { index, addon ->
        result += AddonsListItem.AddonItem(addon)

        if ((index + 1) % 3 == 0) {
            result += AddonsListItem.AdItem
        }
    }
    return result
}

@Composable
fun AddonsList(
    addons: List<ModEntity>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isError: Boolean,
    isEndList: Boolean,
    onClickFavorite: (ModEntity) -> Unit,
    onClickAddon: (ModEntity) -> Unit,
    onLoadMore: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= addons.lastIndex - 3
        }
    }

    var loadTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(shouldLoadMore.value, isLoading, isEndList) {
        if (shouldLoadMore.value && !isLoading && !isEndList && !loadTriggered) {
            loadTriggered = true
            onLoadMore()
        }

        if (!isLoading) {
            loadTriggered = false
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(
            items = buildAddonList(addons),
            key = { it.hashCode() }
        ) { listItem ->

            when (listItem) {
                is AddonsListItem.AddonItem -> {
                    AddonItem(
                        addon = listItem.mod,
                        onClickFavorite = { onClickFavorite(listItem.mod) },
                        onClickAddon = { onClickAddon(listItem.mod) }
                    )
                }

                AddonsListItem.AdItem -> {
                    NativeAdCoordinator.show(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(min = 300.dp)
                    )
                }
            }
        }

        item {
            PaginationLoader(
                isEndList = isEndList,
                isLoading = isLoading,
                isError = isError,
                isEmpty = addons.isEmpty(),
                key = addons.size,
                onLoad = onLoadMore
            )
        }
    }
}
