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
import com.hamit.android.ads.natives.NativeCoordinator
import com.hamit.domain.entity.addon.AddonEntity

sealed class AddonsListItem {
    data class AddonItem(val mod: AddonEntity) : AddonsListItem()
    object AdItem : AddonsListItem()
}

fun buildAddonList(addons: List<AddonEntity>): List<AddonsListItem> {
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
    addons: List<AddonEntity>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isError: Boolean,
    isEndList: Boolean,
    onClickFavorite: (AddonEntity) -> Unit,
    onClickAddon: (AddonEntity) -> Unit,
    onLoadMore: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= addons.lastIndex - 3
        }
    }

    var loadingTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(shouldLoadMore.value, isLoading, isEndList) {
        if (shouldLoadMore.value && !isLoading && !isEndList && !loadingTriggered) {
            loadingTriggered = true
            onLoadMore()
        }

        if (!isLoading) {
            loadingTriggered = false
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
        ) { item ->

            when (item) {
                is AddonsListItem.AddonItem -> {
                    AddonItem(
                        addon = item.mod,
                        onClickLike = { onClickFavorite(item.mod) },
                        onClickAddon = { onClickAddon(item.mod) }
                    )
                }

                AddonsListItem.AdItem -> {
                    NativeCoordinator.show(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(min = 300.dp)
                    )
                }
            }
        }
        item {
            PaginationHandler(
                isEndOfList = isEndList,
                isLoading = isLoading,
                isError = isError,
                isEmptyList = addons.isEmpty(),
                loadKey = addons.size,
                onLoadMore = onLoadMore
            )
        }
    }
}
