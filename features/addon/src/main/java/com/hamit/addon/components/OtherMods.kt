package com.hamit.addon.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.R
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.LocalAppColors
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
internal fun OtherMods(others: List<AddonEntity>?) {
    val colors = LocalAppColors.current
    val shimmer = rememberShimmer(ShimmerBounds.Window)
    Text(
        modifier = Modifier.padding(horizontal = 20.dp),
        text = stringResource(R.string.other_mods),
        fontFamily = AppFonts.CORE,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = colors.title,
    )
    Spacer(Modifier.height(10.dp))
    Row(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        others?.forEach {
            OtherModItem(addon = it,)
        } ?: run {
            repeat(3){
                OtherModShimmerItem(
                    shimmer = shimmer,
                )
            }
        }
    }
}

@Composable
private fun RowScope.OtherModItem(addon: AddonEntity, ) {
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

    Column(
        modifier = Modifier.weight(1f),
    ){
        AsyncImage(
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.shimmer),
            model = request,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(5.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = addon.name,
            fontFamily = AppFonts.CORE,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = colors.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun RowScope.OtherModShimmerItem(shimmer: Shimmer) {
    val colors = LocalAppColors.current

    Column(
        modifier = Modifier.weight(1f)
    ){
        Box(
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .shimmer(shimmer)
                .background(colors.shimmer)
        )
        Spacer(Modifier.height(5.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(shimmer)
                .background(colors.shimmer)
        )

    }
}