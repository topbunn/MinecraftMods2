package com.hamit.ui.components.addon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.theme.LocalAppColors

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
    status: AddonListStatusUi,
    isEndOfList: Boolean,
    onPreload: () -> Unit
) {
    val colors = LocalAppColors.current
    rememberPreloadTrigger(
        state = state,
        itemsCount = addons.size,
        status = status,
        isEndOfList = isEndOfList,
        onPreload = onPreload
    )

    LazyColumn(
        state = state,
        modifier = modifier,
        userScrollEnabled = !(addons.isEmpty() && status == AddonListStatusUi.Loading),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 20.dp)
    ) {
        AddonListContent(
            addons = addons,
            isLoad = status == AddonListStatusUi.Loading
        )

        AddonListFooter(
            colors = colors,
            isEndOfList = isEndOfList,
            isEmpty = addons.isEmpty(),
            status = status,
            onRetry = onPreload
        )
    }
}

