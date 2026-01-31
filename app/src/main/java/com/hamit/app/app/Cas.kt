package com.hamit.app.app

import android.content.Context
import com.cleversolutions.ads.android.CAS
import com.facebook.ads.AdSettings
import com.hamit.app.BuildConfig

fun Context.setupCas() {
    val builder = CAS.buildManager()
        .withCasId(BuildConfig.APPLICATION_ID)
        .withTestAdMode(BuildConfig.DEBUG)
        .withCompletionListener { config ->

        }

    AdSettings.setDataProcessingOptions(arrayOf())
    builder.build(this)
}