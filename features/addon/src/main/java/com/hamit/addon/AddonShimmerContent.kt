package com.hamit.addon

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hamit.ui.theme.LocalAppColors
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun AddonShimmerContent() {
    val colors = LocalAppColors.current
    val shimmer = rememberShimmer(ShimmerBounds.Window)
    Column{
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1.78f)
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmer(shimmer)
                .background(colors.shimmer)
        )
        Spacer(Modifier.height(16.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(30.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(shimmer)
                .background(colors.shimmer)
        )
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(5){
                Box(
                    Modifier
                        .size(56.dp, 26.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmer(shimmer)
                        .background(colors.shimmer)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(25.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(shimmer)
                .background(colors.shimmer)
        )
        Spacer(Modifier.height(10.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(shimmer)
                .background(colors.shimmer)
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(4){
                Box(
                    Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmer(shimmer)
                        .background(colors.shimmer)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1.78f)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmer(shimmer)
                .background(colors.shimmer)
        )
        Spacer(Modifier.height(16.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(25.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(shimmer)
                .background(colors.shimmer)
        )
        Spacer(Modifier.height(10.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            repeat(3){
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmer(shimmer)
                        .background(colors.shimmer)
                )
            }
        }
    }
}