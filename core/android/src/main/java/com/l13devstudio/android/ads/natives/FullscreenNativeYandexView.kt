package com.l13devstudio.android.ads.natives

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.l13devstudio.android.R
import com.l13devstudio.ui.theme.LocalAppColors

@Composable
fun FullscreenNativeYandexView() {
    val context = LocalContext.current
    val adView = remember { NativeYandexController.pop(context, R.layout.yandex_fullscreen_native_ad_layout) } ?: return

    val colors = LocalAppColors.current
    val button = adView.findViewById<TextView>(R.id.yandex_call_to_action)
    button.backgroundTintList = ColorStateList.valueOf(colors.primary.toArgb())

    key("native_ad") {
        AndroidView(
            factory = { adView },
            modifier = Modifier.fillMaxSize()
        )
    }

}