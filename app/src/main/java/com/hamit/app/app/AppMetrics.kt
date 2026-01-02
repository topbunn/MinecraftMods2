package com.hamit.app.app

import android.content.Context
import com.hamit.app.BuildConfig
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

fun Context.setupMetrics(){
    val config = AppMetricaConfig.newConfigBuilder(BuildConfig.METRIC_KEY).build()
    AppMetrica.activate(this, config)
}