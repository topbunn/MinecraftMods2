package com.l13devstudio.app.app

import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.yandex.mobile.ads.instream.MobileInstreamAds

fun Context.setupYandex() {
    MobileInstreamAds.setAdGroupPreloading(true)
    MobileAds.initialize(this) {}
}