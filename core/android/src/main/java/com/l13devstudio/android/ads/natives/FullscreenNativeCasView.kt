package com.l13devstudio.android.ads.natives

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnLayout
import com.cleveradssolutions.sdk.nativead.CASNativeView
import com.l13devstudio.android.R
import com.l13devstudio.ui.theme.LocalAppColors

@Composable
fun FullscreenNativeCasView() {
    val context = LocalContext.current
    val ad = remember { NativeCasController.pop() } ?: return

    val colors = LocalAppColors.current

    val nativeView = remember {
        LayoutInflater.from(context)
            .inflate(
                R.layout.cas_fullscreen_native_ad_layout,
                null
            ) as CASNativeView
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            nativeView.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        update = { view ->

            view.headlineView = view.findViewById(R.id.cas_title)
            view.bodyView = view.findViewById(R.id.cas_body)
            view.advertiserView = view.findViewById(R.id.cas_advertiser)
            view.callToActionView = view.findViewById(R.id.cas_call_to_action)
            view.iconView = view.findViewById(R.id.cas_icon)
            view.mediaView = view.findViewById(R.id.cas_media)
            view.adChoicesView = view.findViewById(R.id.cas_ad_choices)
            view.adLabelView = view.findViewById(R.id.cas_ad_label)

            view.callToActionView?.apply {
                backgroundTintList =
                    ColorStateList.valueOf(colors.primary.toArgb())
                setTextColor(colors.onPrimary.toArgb())
            }

            view.doOnLayout {
                view.setNativeAd(ad)
            }
        }
    )
}