package com.l13devstudio.android.ads.natives

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnAttach
import com.cleveradssolutions.sdk.nativead.CASNativeView
import com.l13devstudio.android.R
import com.l13devstudio.ui.theme.LocalAppColors
import com.l13devstudio.ui.theme.LocalColors

@Composable
fun NativeCasView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colors = LocalAppColors.current

    val nativeAd = remember {
        NativeCasController.pop()
    } ?: return

    AndroidView(
        modifier = modifier,
        factory = {
            LayoutInflater.from(context)
                .inflate(
                    R.layout.cas_native_ad_container,
                    null,
                    false
                )
                .apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                } as CASNativeView
        },
        update = { view ->
            bindCasNative(view)
            styleCasNative(view, colors)

            view.doOnAttach {
                view.setNativeAd(nativeAd)
            }
        }
    )
}

private fun bindCasNative(
    nativeView: CASNativeView
) {
    nativeView.headlineView = nativeView.findViewById(R.id.cas_title)
    nativeView.bodyView = nativeView.findViewById(R.id.cas_body)
    nativeView.advertiserView = nativeView.findViewById(R.id.cas_advertiser)
    nativeView.callToActionView = nativeView.findViewById(R.id.cas_call_to_action)
    nativeView.mediaView = nativeView.findViewById(R.id.cas_media)
    nativeView.iconView = nativeView.findViewById(R.id.cas_icon)
    nativeView.adChoicesView = nativeView.findViewById(R.id.cas_ad_choices)
    nativeView.adLabelView = nativeView.findViewById(R.id.cas_ad_label)
}

private fun styleCasNative(
    view: CASNativeView,
    colors: LocalColors
) {
    view.findViewById<TextView>(R.id.cas_call_to_action)?.apply {
        backgroundTintList = ColorStateList.valueOf(colors.primary.toArgb())
        setTextColor(colors.onPrimary.toArgb())
    }

    view.findViewById<TextView>(R.id.cas_title)
        ?.setTextColor(colors.title.toArgb())

    listOf(
        R.id.cas_advertiser,
        R.id.cas_body,
        R.id.cas_ad_label
    ).forEach {
        view.findViewById<TextView>(it)
            ?.setTextColor(colors.text.toArgb())
    }

    view.backgroundTintList =
        ColorStateList.valueOf(colors.card.toArgb())
}