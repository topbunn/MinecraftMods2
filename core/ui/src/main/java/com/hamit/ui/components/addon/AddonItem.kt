package com.hamit.ui.components.addon

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors
import com.hamit.ui.utils.appDropShadow
import com.valentinilk.shimmer.shimmer

@Composable
fun AddonItem(addon: AddonEntity, onClick: (id: Int) -> Unit) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .appDropShadow(RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(colors.card)
            .clickable{ onClick(addon.id) }
            .padding(vertical = 12.dp),
    ) {
        AddonPreview(addon)
        Spacer(Modifier.height(10.dp))
        AddonTitle(addon)
        Spacer(Modifier.height(4.dp))
        AddonDesc(addon)
        Spacer(Modifier.height(10.dp))
        AddonVersionList(addon.versions)
    }
}

@Composable
private fun AddonVersionList(versions: List<String>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items = versions, key = { it }){
            AddonVersionItem(it)
        }
    }
}



@Composable
private fun AddonDesc(addon: AddonEntity, ) {
    val colors = LocalAppColors.current
    Text(
        modifier = Modifier.padding(horizontal = 12.dp),
        text = addon.desc,
        style = AppTypo.M1,
        color = colors.text,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun AddonTitle(addon: AddonEntity, ) {
    val colors = LocalAppColors.current
    Text(
        modifier = Modifier.padding(horizontal = 12.dp),
        text = addon.name,
        style = AppTypo.H3,
        color = colors.title,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun AddonPreview(addon: AddonEntity, ) {
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
    Box(contentAlignment = Alignment.Center) {
        var isLoad by remember { mutableStateOf(true) }
        val loaderModifier = if (isLoad) Modifier.shimmer() else Modifier
        AsyncImage(
            model = request,
            contentDescription = addon.name,
            contentScale = ContentScale.Crop,
            onState = {
                isLoad = it !is AsyncImagePainter.State.Success
                if (it is AsyncImagePainter.State.Error) {
                    Log.e("Async Image", it.result.throwable.message ?: "???")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .aspectRatio(1.78f)
                .clip(RoundedCornerShape(12.dp))
                .then(loaderModifier)
                .background(colors.shimmer),
        )

    }
}
