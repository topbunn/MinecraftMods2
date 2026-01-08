package com.hamit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hamit.ui.theme.LocalAppColors
import com.hamit.ui.utils.appDropShadow
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer


@Composable
fun AddonShimmer() {
    val colors = LocalAppColors.current
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .appDropShadow(RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(colors.card)
            .padding(vertical = 12.dp),
    ) {
        AddonPreview(shimmer)
        Spacer(Modifier.height(10.dp))
        AddonTitle(shimmer)
        Spacer(Modifier.height(6.dp))
        AddonDesc(shimmer)
        Spacer(Modifier.height(10.dp))
        AddonVersionList(shimmer)
    }
}

@Composable
private fun AddonVersionList(shimmer: Shimmer) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(count = 6){
            AddonVersionItem(shimmer)
        }
    }
}

@Composable
private fun AddonVersionItem(shimmer: Shimmer) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .height(30.dp)
            .width(65.dp)
            .clip(RoundedCornerShape(6.dp))
            .shimmer(shimmer)
            .background(colors.shimmer),
    )
}

@Composable
private fun AddonDesc(shimmer: Shimmer) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(6.dp))
            .shimmer(shimmer)
            .background(colors.shimmer),
    )
}

@Composable
private fun AddonTitle(shimmer: Shimmer) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(6.dp))
            .shimmer(shimmer)
            .background(colors.shimmer),
    )
}

@Composable
private fun AddonPreview(shimmer: Shimmer) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .aspectRatio(1.78f)
            .clip(RoundedCornerShape(12.dp))
            .shimmer(shimmer)
            .background(colors.shimmer),
    )
}
