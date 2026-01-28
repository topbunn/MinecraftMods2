//package com.hamit.android.ads.natives
//
//import android.content.res.ColorStateList
//import android.widget.Button
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.key
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.viewinterop.AndroidView
//import com.hamit.android.R
//import com.hamit.ui.theme.LocalAppColors
//
//@Composable
//fun FullscreenNativeApplovinView() {
//    val context = LocalContext.current
//    val adView = remember { NativeApplovinController.pop(context, R.layout.applovin_fullscreen_native_ad_layout) } ?: return
//
//    val colors = LocalAppColors.current
//    val button = adView.findViewById<Button>(R.id.applovin_call_to_action)
//    button.backgroundTintList = ColorStateList.valueOf(colors.primary.toArgb())
//
//    key("native_ad") {
//        AndroidView(
//            factory = { adView },
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//
//}