package com.hamit.app.app

import android.content.Context
import com.cleversolutions.ads.android.CAS
import com.hamit.app.BuildConfig

fun Context.setupCas() {
    val builder = CAS.buildManager()
        .withCasId(BuildConfig.CAS_SDK_KEY)
        .withTestAdMode(BuildConfig.DEBUG)
        .withCompletionListener { config ->

        }
    builder.build(this)
}