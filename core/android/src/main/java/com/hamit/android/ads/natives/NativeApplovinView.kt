//package com.hamit.android.ads.natives
//
//import android.content.res.ColorStateList
//import android.view.View
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.key
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.viewinterop.AndroidView
//import com.hamit.android.R
//import com.hamit.ui.theme.LocalAppColors
//
//@Composable
//fun NativeApplovinView(modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    val adView = remember { NativeApplovinController.pop(context, R.layout.applovin_native_ad_container) } ?: return
//
//    with(adView){
//        val colors = LocalAppColors.current
//
//        val button = findViewById<Button>(R.id.applovin_call_to_action)
//        button.backgroundTintList = ColorStateList.valueOf(colors.primary.toArgb())
//        button.setTextColor(ColorStateList.valueOf(colors.onPrimary.toArgb()))
//
//        val card = findViewById<View>(R.id.applovin_native_ad_view)
//        card.backgroundTintList = ColorStateList.valueOf(colors.card.toArgb())
//
//        val feedbackIcon = findViewById<ImageView>(R.id.applovin_feedback)
//        feedbackIcon.setColorFilter(colors.background.toArgb(), android.graphics.PorterDuff.Mode.SRC_IN)
//
//        val title = findViewById<TextView>(R.id.applovin_title)
//        title.setTextColor(ColorStateList.valueOf(colors.title.toArgb()))
//
//        val advertiser =  findViewById<TextView>(R.id.applovin_advertiser)
//        advertiser.setTextColor(ColorStateList.valueOf(Color.White.toArgb()))
//
//        val body = findViewById<TextView>(R.id.applovin_body)
//        body.setTextColor(ColorStateList.valueOf(colors.text.toArgb()))
//
//    }
//
//    key("native_ad") {
//        AndroidView(
//            factory = { adView },
//            modifier = modifier
//        )
//    }
//}
