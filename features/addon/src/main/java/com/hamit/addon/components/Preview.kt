package com.hamit.addon.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.theme.LocalAppColors
import com.valentinilk.shimmer.shimmer

@Composable
internal fun Preview(addon: AddonEntity) {
    val colors = LocalAppColors.current
    val context = LocalContext.current

    val request = remember(addon.preview) {
        ImageRequest.Builder(context)
            .data(addon.preview)
            .crossfade(false)
            .memoryCacheKey(addon.preview)
            .diskCacheKey(addon.preview)
            .build()
    }

    var isLoad by remember { mutableStateOf(true) }
    val loaderModifier = if (isLoad) Modifier
        .background(colors.shimmer)
        .shimmer() else Modifier

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    )
    {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.78f)
                .clip(RoundedCornerShape(12.dp))
                .then(loaderModifier),
            model = request,
            contentDescription = addon.name,
            contentScale = ContentScale.Crop,
            onState = {
                if (it !is AsyncImagePainter.State.Loading) {
                    isLoad = false
                }
                if (it is AsyncImagePainter.State.Error) {
                    Log.e("Async Image", it.result.throwable.message ?: "???")
                }
            },
        )

    }
}