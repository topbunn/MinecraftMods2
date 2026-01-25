package com.hamit.ad

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hamit.android.ads.natives.NativeCoordinator
import com.hamit.navigation.Destination
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdScreen(private val nextDestination: Destination): Screen, Parcelable{

    @Composable
    override fun Content() {
        val nextScreen = rememberScreen(nextDestination)
        val navigator = LocalNavigator.currentOrThrow

        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xff14161B))
                .systemBarsPadding()
        ){
            NativeCoordinator.show(NativeCoordinator.ViewAdType.Fullscreen)
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 20.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(0.5f))
                    .clickable {
                        navigator.replace(nextScreen)
                    }
                    .padding(6.dp),
                painter = painterResource(com.hamit.ui.R.drawable.ic_close),
                contentDescription = "close ad",
                tint = Color.White
            )
        }
    }

}
