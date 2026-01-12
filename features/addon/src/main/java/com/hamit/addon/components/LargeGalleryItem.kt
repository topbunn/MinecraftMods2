package com.hamit.addon.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hamit.ui.theme.LocalAppColors

@Composable
internal fun LargeGalleryItem(link: String, width: Dp) {
    val colors = LocalAppColors.current
    val context = LocalContext.current

    val request = remember(link) {
        ImageRequest.Builder(context)
            .data(link)
            .crossfade(false)
            .memoryCacheKey(link)
            .diskCacheKey(link)
            .build()
    }

    AsyncImage(
        modifier = Modifier
            .width(width)
            .aspectRatio(1.78f)
            .clip(RoundedCornerShape(16.dp))
            .background(colors.shimmer),
        model = request,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}