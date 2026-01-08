package com.hamit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import com.hamit.domain.entity.addon.AddonEntity

sealed class AddonListItemType {
    data class Addon(val addon: AddonEntity) : AddonListItemType()
    object Ad : AddonListItemType()
}

fun constructList(addons: List<AddonEntity>): List<AddonListItemType> {
    val result = ArrayList<AddonListItemType>()
    addons.forEachIndexed { index, mod ->
        result += AddonListItemType.Addon(mod)

        if ((index + 1) % 3 == 0) {
            result += AddonListItemType.Ad
        }
    }
    return result
}

@Composable
fun AddonList(
    addons: List<AddonEntity>,
    state: LazyListState,
    modifier: Modifier = Modifier,
    isLoad: Boolean,
    isError: Boolean,
    isEndOfList: Boolean,
    onPreload: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisible = state.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= addons.lastIndex - 3
        }
    }

    var loadingTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(shouldLoadMore.value, isLoad, isEndOfList) {
        if (shouldLoadMore.value && !isLoad && !isEndOfList && !loadingTriggered) {
            loadingTriggered = true
            onPreload()
        }

        if (!isLoad) {
            loadingTriggered = false
        }
    }

    LazyColumn(
        state = state,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 20.dp)
    ) {
        items(
            items = constructList(addons),
            key = { it.hashCode() }
        ) { item ->

            when (item) {
                is AddonListItemType.Addon -> {
                    AddonItem(
                        addon = item.addon,
                    )
                }
                else -> {}
            }
        }
        item {
            PaginationLoader(
                isEndOfList = isEndOfList,
                isLoad = isLoad,
                isError = isError,
                listIsEmpty = addons.isEmpty(),
                loadKey = addons.size,
                onPreload = onPreload
            )
        }
    }
}
