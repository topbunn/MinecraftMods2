package com.hamit.ad

import android.content.res.ColorStateList
import android.os.Parcelable
import android.widget.Button
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
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hamit.android.R
import com.hamit.android.ads.natives.NativeApplovinController
import com.hamit.navigation.Destination
import com.hamit.ui.theme.LocalAppColors
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdScreen(private val nextDestination: Destination): Screen, Parcelable{

    @Composable
    override fun Content() {
        val nextScreen = rememberScreen(nextDestination)
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current

        Box(
            Modifier.fillMaxSize()
                .background(Color(0xff14161B))
                .systemBarsPadding()
        ){
            val adView = remember { NativeApplovinController.pop(context, R.layout.applovin_fullscreen_native_ad_layout) }
            adView?.let {
                val colors = LocalAppColors.current
                val button = adView.findViewById<Button>(R.id.applovin_call_to_action)
                button.backgroundTintList = ColorStateList.valueOf(colors.primary.toArgb())
                key("native_ad") {
                    AndroidView(
                        factory = { adView },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 20.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray.copy(0.2f))
                    .clickable{
                        navigator.replace(nextScreen)
                    }.padding(6.dp),
                painter = painterResource(com.hamit.ui.R.drawable.ic_close),
                contentDescription = "close ad",
                tint = Color.LightGray
            )
        }
    }

}
