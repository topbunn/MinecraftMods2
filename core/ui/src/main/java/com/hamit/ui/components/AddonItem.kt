package com.hamit.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.R

@Composable
fun AddonItem(addon: AddonEntity, onClickLike: () -> Unit, onClickAddon: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(AppColors.GRAY_BG)
            .clickable { onClickAddon() }
            .padding(10.dp),
    ) {
        val context = LocalContext.current

        val request = remember(addon.image) {
            ImageRequest.Builder(context)
                .data(addon.image)
                .crossfade(false)
                .memoryCacheKey(addon.image)
                .diskCacheKey(addon.image)
                .build()
        }

        Box(contentAlignment = Alignment.Center) {
            var isLoading by remember { mutableStateOf(true) }
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.7f)
                    .clip(RoundedCornerShape(6.dp)),
                model = request,
                contentDescription = "image mod",
                contentScale = ContentScale.Crop,
                onState = {
                    if (it !is AsyncImagePainter.State.Loading) {
                        isLoading = false
                    }
                    if (it is AsyncImagePainter.State.Error) {
                        Log.e("Error Async Image", it.result.throwable.message ?: "Неизвестно")
                    }
                }
            )
            if (isLoading) {
                CircularProgressIndicator(
                    color = AppColors.WHITE,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                modifier = Modifier.weight(1f),
                text = addon.title,
                style = AppTypo.APP_TEXT,
                fontSize = 18.sp,
                color = AppColors.WHITE,
                fontFamily = AppFonts.CORE.BOLD,
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .clickableEmpty { onClickLike() },
                painter = painterResource(if (addon.isLike) R.drawable.ic_mine_like_filled else R.drawable.ic_mine_like_stroke),
                contentDescription = "status favorite mod",
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = addon.description,
            style = AppTypo.APP_TEXT,
            fontSize = 15.sp,
            color = AppColors.GRAY,
            fontFamily = AppFonts.CORE.MEDIUM,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
        )

    }
}
