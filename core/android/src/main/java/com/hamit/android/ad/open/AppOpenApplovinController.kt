package com.hamit.android.ad.open

import android.app.Activity
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAppOpenAd
import kotlinx.coroutines.*
import kotlin.math.min
import kotlin.math.pow

object AppOpenApplovinController : MaxAdListener {

    private lateinit var appOpenAd: MaxAppOpenAd
    private var retryCount = 0
    private var isShowingAd = false
    private var isInitialized = false

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    fun init(activity: Activity, adId: String) {
        log { "Initializing AppOpenAd with adId=$adId" }

        if (isInitialized) {
            log { "Already initialized — skipping" }
            return
        }
        isInitialized = true

        appOpenAd = MaxAppOpenAd(adId, activity)
        appOpenAd.setListener(this)

        loadAd()
    }

    private fun loadAd() {
        log { "Starting AppOpenAd load…" }
        appOpenAd.loadAd()
    }

    fun showIfReady() {
        log { "Checking if AppOpenAd is ready…" }

        if (::appOpenAd.isInitialized && appOpenAd.isReady && !isShowingAd) {
            isShowingAd = true
            log { "Ad ready — showing AppOpenAd…" }
            appOpenAd.showAd()
        } else {
            log { "Ad not ready or already showing" }
        }
    }

    override fun onAdLoaded(ad: MaxAd) {
        log { "AppOpenAd successfully loaded" }
        retryCount = 0
    }

    override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
        retryCount++

        val delayMs = 2.0.pow(min(6, retryCount)).toLong() * 1000
        log { "Load failed: ${error.message}. Retry in $delayMs ms" }

        scope.launch {
            delay(delayMs)
            loadAd()
        }
    }

    override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
        log { "Display failed: ${error.message}" }
        isShowingAd = false
        loadAd()
    }

    override fun onAdDisplayed(ad: MaxAd) {
        log { "AppOpenAd displayed" }
    }

    override fun onAdHidden(ad: MaxAd) {
        log { "AppOpenAd hidden" }
        isShowingAd = false
        loadAd()
    }

    override fun onAdClicked(ad: MaxAd) {
        log { "AppOpenAd clicked" }
    }

    fun pause() {
        log { "Pause — cancelling coroutines" }
        job.cancelChildren()
    }

    fun resume() {
        log { "Resume — load if not ready" }
        if (::appOpenAd.isInitialized && !appOpenAd.isReady) {
            loadAd()
        }
    }

    fun destroy() {
        log { "Destroy — clearing listener and coroutines" }
        job.cancel()

        if (::appOpenAd.isInitialized) {
            appOpenAd.setListener(null)
        }
    }

    private fun log(message: () -> String) {
        Log.d("APP_OPEN_APPL", message())
    }
}
