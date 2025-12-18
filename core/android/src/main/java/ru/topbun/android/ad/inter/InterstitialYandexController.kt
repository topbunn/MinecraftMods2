package ru.topbun.android.ad.inter

import android.app.Activity
import android.content.Context
import android.util.Log
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import kotlinx.coroutines.*
import kotlin.math.min
import kotlin.math.pow


object InterstitialYandexController :
    InterstitialAdLoadListener,
    InterstitialAdEventListener {

    private lateinit var adLoader: InterstitialAdLoader
    private var loadedAd: InterstitialAd? = null
    private var adUnitId: String = ""

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
        log { "Init Yandex interstitial: $adId" }

        if (isInitialized) {
            log { "Already initialized — skip" }
            return
        }
        isInitialized = true

        adUnitId = adId
        adLoader = InterstitialAdLoader(context)
        adLoader.setAdLoadListener(this)

        requestLoad()
    }

    private fun requestLoad() {
        if (!isInitialized) return

        if (isPaused) {
            log { "Load cancelled — paused" }
            return
        }

        if (isLoading) {
            log { "Load already in progress — skip" }
            return
        }

        if (loadedAd != null) {
            log { "Already loaded — no need to load again" }
            return
        }

        log { "Start Yandex interstitial load" }
        isLoading = true

        retryJob?.cancel()

        val request = AdRequestConfiguration.Builder(adUnitId).build()
        adLoader.loadAd(request)
    }

    fun show(activity: Activity) {
        log { "Show attempt" }

        val ad = loadedAd
        if (ad != null) {
            log { "Ready — showing" }
            ad.setAdEventListener(this)
            ad.show(activity)
        } else {
            log { "Not ready — request load" }
            requestLoad()
        }
    }

    override fun onAdLoaded(ad: InterstitialAd) {
        log { "Yandex interstitial loaded" }
        loadedAd = ad
        isLoading = false
        retryCount = 0
        adReadyListener?.invoke()
    }

    override fun onAdFailedToLoad(error: AdRequestError) {
        log { "Load failed: ${error.description}" }
        loadedAd = null
        isLoading = false
        retryCount++

        val delayMs = (2.0.pow(min(6, retryCount))).toLong() * 1000
        log { "Retry in $delayMs ms" }

        retryJob?.cancel()
        retryJob = scope.launch {
            delay(delayMs)
            requestLoad()
        }
    }

    override fun onAdShown() {
        log { "Yandex interstitial shown" }
    }

    override fun onAdDismissed() {
        log { "Yandex interstitial dismissed" }
        loadedAd = null

        if (!isPaused) {
            requestLoad()
        } else {
            log { "Skip reload — paused" }
        }
    }

    override fun onAdFailedToShow(error: AdError) {
        log { "Show failed: ${error.description}" }
        loadedAd = null
        isLoading = false
        requestLoad()
    }

    override fun onAdClicked() {
        log { "Yandex interstitial clicked" }
    }

    override fun onAdImpression(data: ImpressionData?) {
        log { "Yandex interstitial impression recorded" }
    }

    fun pause() {
        log { "PAUSE — stop loading" }
        isPaused = true
        retryJob?.cancel()
        isLoading = false
    }

    fun resume() {
        log { "RESUME — resume loading if needed" }

        isPaused = false

        if (loadedAd == null) {
            requestLoad()
        }
    }

    fun destroy() {
        log { "Destroy — clear listeners and jobs" }
        isPaused = true
        retryJob?.cancel()
        isLoading = false

        loadedAd?.setAdEventListener(null)
        loadedAd = null

        scope.coroutineContext.cancelChildren()
    }

    private fun log(msg: () -> String) {
        Log.d("YANDEX_INTER_CTRL", msg())
    }
}
