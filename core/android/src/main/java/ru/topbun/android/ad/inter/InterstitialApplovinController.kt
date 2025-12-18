package ru.topbun.android.ad.inter

import android.content.Context
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import kotlinx.coroutines.*

import kotlin.math.min
import kotlin.math.pow

object InterstitialApplovinController : MaxAdListener {

    private lateinit var adUnit: MaxInterstitialAd
    private var isInitialized = false

    @Volatile private var isLoading = false
    @Volatile private var isPaused = false

    private var retryCount = 0

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var retryJob: Job? = null

    private var adReadyListener: (() -> Unit)? = null

    fun setAdReadyListener(callback: () -> Unit) {
        adReadyListener = callback
    }

    fun removeAdReadyListener() {
        adReadyListener = null
    }

    fun init(context: Context, adId: String) {
        log { "Init interstitial with adId=$adId" }
        if (isInitialized) {
            log { "Already initialized — skip" }
            return
        }
        isInitialized = true

        adUnit = MaxInterstitialAd(adId, context)
        adUnit.setListener(this)

        requestInterstitialLoad()
    }

    private fun requestInterstitialLoad() {

        if (!::adUnit.isInitialized){
            log { "Ad unit not initialized yet" }
            return
        }

        if (isPaused) {
            log { "Load cancelled — controller paused" }
            return
        }

        if (isLoading) {
            log { "Load already in progress — skip" }
            return
        }

        if (::adUnit.isInitialized && adUnit.isReady) {
            log { "Interstitial already loaded — no need to reload" }
            return
        }

        log { "Start interstitial loading" }
        isLoading = true

        retryJob?.cancel()
        adUnit.loadAd()
    }

    fun show() {
        log { "Show request received" }

        if (::adUnit.isInitialized && adUnit.isReady) {
            log { "Interstitial ready — showing" }
            adUnit.showAd()
        } else {
            log { "Not ready — load attempt" }
            requestInterstitialLoad()
        }
    }

    override fun onAdLoaded(ad: MaxAd) {
        log { "Interstitial loaded" }
        isLoading = false
        retryCount = 0
        adReadyListener?.invoke()
    }

    override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
        log { "Load failed: ${error.message}" }
        isLoading = false
        retryCount++

        val delayMs = (2.0.pow(min(6, retryCount))).toLong() * 1000
        log { "Next retry in $delayMs ms" }

        retryJob?.cancel()
        retryJob = scope.launch {
            delay(delayMs)
            requestInterstitialLoad()
        }
    }

    override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
        log { "Display failed: ${error.message}" }
        isLoading = false
        requestInterstitialLoad()
    }

    override fun onAdDisplayed(ad: MaxAd) {
        log { "Interstitial displayed" }
    }

    override fun onAdHidden(ad: MaxAd) {
        log { "Interstitial hidden" }
        isLoading = false

        if (!isPaused) {
            requestInterstitialLoad()
        } else {
            log { "Skip reload — controller paused" }
        }
    }

    override fun onAdClicked(ad: MaxAd) {
        log { "Interstitial clicked" }
    }

    fun pause() {
        log { "PAUSE — stop loading" }
        isPaused = true
        retryJob?.cancel()
        isLoading = false
    }

    fun resume() {
        log { "RESUME — check and load if necessary" }
        isPaused = false

        if (::adUnit.isInitialized && !adUnit.isReady) {
            requestInterstitialLoad()
        }
    }

    fun destroy() {
        log { "Destroy — clearing listener & coroutines" }
        isPaused = true
        retryJob?.cancel()
        isLoading = false

        if (::adUnit.isInitialized) {
            adUnit.setListener(null)
        }

        scope.coroutineContext.cancelChildren()
    }

    private fun log(msg: () -> String) {
        Log.d("INTER_AD_CTRL", msg())
    }
}
