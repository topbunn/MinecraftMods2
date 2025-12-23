package com.hamit.android.ad.open

import android.app.Activity
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.yandex.mobile.ads.appopenad.*
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import kotlin.math.min

object AppOpenYandexController : AppOpenAdLoadListener {

    private lateinit var loader: AppOpenAdLoader
    private lateinit var adUnitId: String
    private var currentAd: AppOpenAd? = null

    private var adState = AdState.IDLE
    private var retryCount = 0
    private var lastLoadTime = 0L

    private val handler = Handler(Looper.getMainLooper())

    private const val MIN_LOAD_INTERVAL = 800L
    private const val BASE_BACKOFF = 1000L
    private const val MAX_BACKOFF_EXP = 6

    private enum class AdState {
        IDLE,
        LOADING,
        READY,
        SHOWING
    }

    fun init(application: Application, adId: String) {
        log { "Initializing Yandex AppOpenAd with adUnitId=$adId" }

        loader = AppOpenAdLoader(application)
        loader.setAdLoadListener(this)
        adUnitId = adId

        load()
    }

    fun show(activity: Activity) {
        log { "Request to show Yandex AppOpenAd. state=$adState, adLoaded=${currentAd != null}" }

        if (adState == AdState.READY && currentAd != null) {
            log { "Showing Yandex AppOpenAd" }
            adState = AdState.SHOWING
            currentAd!!.setAdEventListener(eventListener)
            currentAd!!.show(activity)
        } else {
            log { "Ad not ready, loading..." }
            load()
        }
    }

    private fun load() {
        if (!::loader.isInitialized) {
            log { "Error: loader not initialized" }
            return
        }

        if (adState == AdState.LOADING) {
            log { "Ad is already loading — skip" }
            return
        }

        val now = System.currentTimeMillis()
        if (now - lastLoadTime < MIN_LOAD_INTERVAL) {
            val wait = MIN_LOAD_INTERVAL - (now - lastLoadTime)
            log { "Too soon to reload — waiting $wait ms" }
            handler.postDelayed({ load() }, wait)
            return
        }

        log { "Starting Yandex AppOpenAd load" }
        adState = AdState.LOADING

        val config = AdRequestConfiguration.Builder(adUnitId).build()
        loader.loadAd(config)
    }

    override fun onAdLoaded(loadedAd: AppOpenAd) {
        log { "Yandex AppOpenAd successfully loaded" }
        currentAd = loadedAd
        adState = AdState.READY
        retryCount = 0
        lastLoadTime = System.currentTimeMillis()
    }

    override fun onAdFailedToLoad(error: AdRequestError) {
        log { "Failed to load Yandex AppOpenAd: ${error.description}" }

        currentAd = null
        adState = AdState.IDLE

        retryCount++
        val delay = calculateBackoffDelay(retryCount)
        log { "Retrying load in $delay ms (attempt $retryCount)" }

        handler.postDelayed({ load() }, delay)
    }

    private fun calculateBackoffDelay(attempt: Int): Long {
        val exp = min(MAX_BACKOFF_EXP, attempt)
        val delay = BASE_BACKOFF * (1L shl exp)
        log { "Backoff delay: $delay ms" }
        return delay
    }

    private val eventListener = object : AppOpenAdEventListener {

        override fun onAdShown() {
            log { "Yandex AppOpenAd shown" }
            adState = AdState.SHOWING
        }

        override fun onAdDismissed() {
            log { "Yandex AppOpenAd dismissed" }
            currentAd = null
            adState = AdState.IDLE
            load()
        }

        override fun onAdFailedToShow(error: AdError) {
            log { "Failed to show Yandex AppOpenAd: ${error.description}" }
            currentAd = null
            adState = AdState.IDLE
            handler.postDelayed({ load() }, BASE_BACKOFF)
        }

        override fun onAdClicked() {
            log { "Yandex AppOpenAd clicked" }
        }

        override fun onAdImpression(data: ImpressionData?) {
            log { "Yandex AppOpenAd impression" }
        }
    }

    fun destroy() {
        log { "Destroying AppOpenYandexController" }

        currentAd?.setAdEventListener(null)
        currentAd = null
        adState = AdState.IDLE
        retryCount = 0
        handler.removeCallbacksAndMessages(null)
    }

    private fun log(message: () -> String) {
        Log.d("APP_OPEN_YANDEX", message())
    }
}
