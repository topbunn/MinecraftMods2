package com.l13devstudio.app.app

import android.content.Context
import com.cleversolutions.ads.android.CAS
import com.facebook.ads.AdSettings
import com.l13devstudio.app.BuildConfig

fun Context.setupCas() {
    val builder = CAS.buildManager()
        .withCasId(BuildConfig.APPLICATION_ID)
        .withCompletionListener {}
    AdSettings.setDataProcessingOptions(arrayOf())
    builder.build(this)
}