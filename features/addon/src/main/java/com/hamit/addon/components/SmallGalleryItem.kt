package com.hamit.addon.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hamit.ui.theme.LocalAppColors

@Composable
internal fun SmallGalleryItem(
    link: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
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

    Box(
        modifier = Modifier
            .border(
                width = 1.5.dp,
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) colors.primary else Color.Transparent
            )
            .padding(3.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.shimmer),
            model = request,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}
