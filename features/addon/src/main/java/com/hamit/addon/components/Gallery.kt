package com.hamit.addon.components

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.R
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.LocalAppColors
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.coroutines.launch

@Composable
internal fun Gallery(addon: AddonEntity) {
    if (addon.images.isEmpty()) return
    val scope = rememberCoroutineScope()
    val shimmer = rememberShimmer(ShimmerBounds.Window)
    val colors = LocalAppColors.current

    val smallListState = rememberLazyListState()
    val largeListState = rememberLazyListState()

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth - 40.dp

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    val snapFlingBehavior = rememberSnapFlingBehavior(
        lazyListState = largeListState
    )

    LaunchedEffect(largeListState) {
        snapshotFlow { largeListState.firstVisibleItemIndex }
            .collect { index ->
                selectedIndex = index
                smallListState.animateScrollToItem(index)
            }
    }

    Text(
        modifier = Modifier.padding(horizontal = 20.dp),
        text = stringResource(R.string.gallery),
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = colors.title,
    )

    Spacer(Modifier.height(10.dp))

    LazyRow(
        state = smallListState,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(addon.images) { index, image ->
            SmallGalleryItem(
                shimmer = shimmer,
                link = image,
                isSelected = selectedIndex == index
            ) {
                selectedIndex = index
                scope.launch { largeListState.scrollToItem(index) }
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    LazyRow(
        state = largeListState,
        flingBehavior = snapFlingBehavior,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(addon.images) {
            LargeGalleryItem(
                shimmer = shimmer,
                link = it,
                width = itemWidth
            )
        }
    }
}