package com.l13devstudio.android.ads.natives

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.l13devstudio.android.R
import com.l13devstudio.ui.theme.LocalAppColors
import com.l13devstudio.ui.theme.LocalColors

@Composable
fun NativeYandexView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colors = LocalAppColors.current

    val adView = remember {
        NativeYandexController.pop(
            context,
            R.layout.yandex_native_ad_container
        )
    } ?: return

    AndroidView(
        modifier = modifier,
        factory = { adView },
        update = { view ->
            styleYandexNative(view, colors)
        }
    )
}

private fun styleYandexNative(
    view: View,
    colors: LocalColors
) {
    view.findViewById<TextView>(R.id.yandex_call_to_action)?.apply {
        backgroundTintList =
            ColorStateList.valueOf(colors.primary.toArgb())
        setTextColor(colors.onPrimary.toArgb())
    }

    view.findViewById<View>(R.id.native_ad_view)
        ?.backgroundTintList =
        ColorStateList.valueOf(colors.card.toArgb())

    view.findViewById<ImageView>(R.id.yandex_feedback)
        ?.setColorFilter(
            colors.background.toArgb(),
            android.graphics.PorterDuff.Mode.SRC_IN
        )

    view.findViewById<TextView>(R.id.yandex_title)
        ?.setTextColor(colors.title.toArgb())

    listOf(
        R.id.yandex_warning,
        R.id.yandex_body
    ).forEach {
        view.findViewById<TextView>(it)
            ?.setTextColor(colors.text.toArgb())
    }

    listOf(
        R.id.yandex_domain,
        R.id.yandex_sponsored
    ).forEach {
        view.findViewById<TextView>(it)
            ?.setTextColor(Color.White.toArgb())
    }
}
