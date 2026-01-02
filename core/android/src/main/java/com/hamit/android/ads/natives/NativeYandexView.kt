package com.hamit.android.ads.natives

import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun NativeYandexView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val adView = remember {
        FrameLayout(context).apply {
            val view = NativeYandexController.pop()
            if (view != null) addView(view)
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { adView }
    )
}
