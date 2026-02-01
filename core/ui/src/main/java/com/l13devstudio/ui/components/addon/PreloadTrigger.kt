package com.l13devstudio.ui.components.addon

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.l13devstudio.domain.entity.AddonListStatusUi

@Composable
internal fun rememberPreloadTrigger(
    state: LazyListState,
    itemsCount: Int,
    status: AddonListStatusUi,
    isEndOfList: Boolean,
    onPreload: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisible = state.layoutInfo.visibleItemsInfo
                .lastOrNull()
                ?.index ?: 0

            lastVisible >= itemsCount - 3
        }
    }

    var loadingTriggered by remember { mutableStateOf(false) }

    val isLoading = status == AddonListStatusUi.Loading
    val isError = status is AddonListStatusUi.Error

    LaunchedEffect(shouldLoadMore.value, isLoading, isEndOfList, isError) {
        if (shouldLoadMore.value && !isLoading && !isEndOfList && !loadingTriggered && !isError) {
            loadingTriggered = true
            onPreload()
        }

        if (!isLoading) {
            loadingTriggered = false
        }
    }
}
