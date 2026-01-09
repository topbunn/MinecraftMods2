package com.hamit.ui.components.addon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.R
import com.hamit.ui.components.AppButton
import com.hamit.ui.theme.AppTypo
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
    isLoad: Boolean,
    isError: Boolean,
    isEndOfList: Boolean,
    onPreload: () -> Unit
) {
    val colors = LocalAppColors.current
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
        userScrollEnabled = !(addons.isEmpty() && isLoad),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 20.dp)
    ) {
        if (addons.isEmpty() && isLoad){
            items(count = 3){
                AddonShimmer()
            }
        } else {
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

            when {
                isError -> item{
                    AppButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.retry)
                    ) { onPreload() }
                }

                !isEndOfList && isLoad -> item{
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colors.text,
                            strokeWidth = 2.5.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                !isLoad && !isError && addons.isEmpty() -> item{
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.the_list_is_empty),
                            textAlign = TextAlign.Center,
                            style = AppTypo.M1,
                            color = colors.text,
                        )
                    }
                }
            }
        }
    }
}
