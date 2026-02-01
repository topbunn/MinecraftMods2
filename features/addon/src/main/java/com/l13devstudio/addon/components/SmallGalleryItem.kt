package com.l13devstudio.addon.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.l13devstudio.ui.theme.LocalAppColors
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
internal fun SmallGalleryItem(
    shimmer: Shimmer,
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


    var isLoad by remember { mutableStateOf(true) }
    val loaderModifier = if (isLoad) Modifier.shimmer(shimmer) else Modifier


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
                .then(loaderModifier)
                .background(colors.shimmer),
            model = request,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onState = {
                isLoad = it !is AsyncImagePainter.State.Success
                if (it is AsyncImagePainter.State.Error) {
                    Log.e("Async Image", it.result.throwable.message ?: "???")
                }
            },
        )
    }
}
