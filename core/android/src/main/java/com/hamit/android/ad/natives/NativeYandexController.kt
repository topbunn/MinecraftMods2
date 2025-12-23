package com.hamit.android.ad.natives

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.nativeads.*
import kotlinx.coroutines.*
import com.hamit.android.R
import kotlin.math.min
import kotlin.math.pow

object NativeYandexController {

    private const val MAX_POOL = 5

    private var adLoader: NativeAdLoader? = null
    private val adPool = ArrayDeque<NativeAdView>(MAX_POOL)
    private var adUnitId: String? = null

    private var isInitialized = false
    private var retryCount = 0
    private var pendingLoads = 0

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun init(context: Context, adUnitId: String) {
        log { "Init Yandex native ads, adUnitId=$adUnitId" }
        if (isInitialized) return
        NativeYandexController.adUnitId = adUnitId
        isInitialized = true

        adLoader = NativeAdLoader(context).apply {
            setNativeAdLoadListener(object : NativeAdLoadListener {
                override fun onAdLoaded(ad: NativeAd) {
                    retryCount = 0
                    pendingLoads--

                    val adView = createAdView(context)
                    bindAdToView(ad, adView)

                    if (adPool.size < MAX_POOL) {
                        adPool.add(adView)
                        log { "Native AdView added to pool (size=${adPool.size})" }
                    } else {
                        log { "Pool full — extra Native Ad skipped" }
                    }

                    preloadNext()
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    retryCount++
                    pendingLoads--
                    val delayMs = 2.0.pow(min(retryCount, 6)).toLong() * 1000
                    log { "Yandex native load failed: ${error.description}. Retry in $delayMs ms" }

                    scope.launch {
                        delay(delayMs)
                        preloadNext()
                    }
                }
            })
        }
    }

    private fun createAdView(context: Context): NativeAdView {
        return LayoutInflater.from(context)
            .inflate(R.layout.yandex_native_ad_container, null) as NativeAdView
    }

    private fun bindAdToView(ad: NativeAd, adView: NativeAdView) {
        val binder = NativeAdViewBinder.Builder(adView)
            .setTitleView(adView.findViewById(R.id.title))
            .setDomainView(adView.findViewById(R.id.domain))
            .setWarningView(adView.findViewById(R.id.warning))
            .setSponsoredView(adView.findViewById(R.id.sponsored))
            .setFeedbackView(adView.findViewById(R.id.feedback))
            .setCallToActionView(adView.findViewById(R.id.call_to_action))
            .setMediaView(adView.findViewById(R.id.media))
            .setIconView(adView.findViewById(R.id.icon))
            .setPriceView(adView.findViewById(R.id.price))
            .setBodyView(adView.findViewById(R.id.body))
            .build()

        try {
            ad.bindNativeAd(binder)
            ad.setNativeAdEventListener(object : NativeAdEventListener {
                override fun onAdClicked() {
                    log { "Yandex Native Ad clicked" }
                }

                override fun onLeftApplication() {}
                override fun onReturnedToApplication() {}
                override fun onImpression(impressionData: ImpressionData?) {
                    log { "Yandex Native Ad impression" }
                }
            })
        } catch (_: NativeAdException) { }
    }

    private fun preloadNext() {
        if (!isInitialized) return
        if (adPool.size + pendingLoads >= MAX_POOL) return

        pendingLoads++
        log { "Preloading next Yandex native ad (pool=${adPool.size}, pending=$pendingLoads)" }

        adUnitId?.let {
            val request = NativeAdRequestConfiguration.Builder(it).build()
            adLoader?.loadAd(request)
        } ?: log { "adUnitId is null, cannot preload" }
    }

    fun preload() {
        if (!isInitialized) {
            log { "Yandex native ads not initialized" }
            return
        }

        log { "Starting Yandex native preloading" }
        repeat(MAX_POOL) { preloadNext() }
    }

    fun popAd(): NativeAdView? {
        if (!isInitialized) return null

        val adView = adPool.removeFirstOrNull()
        if (adView != null) {
            log { "Native AdView popped from pool (remaining=${adPool.size})" }
            preloadNext()
        } else {
            log { "Pool empty — returning null" }
        }
        return adView
    }

    fun destroy() {
        log { "Destroy NativeYandexController" }
        scope.coroutineContext.cancel()
        adPool.clear()
        adLoader = null
        isInitialized = false
        retryCount = 0
        pendingLoads = 0
    }

    private fun log(message: () -> String) {
        Log.d("NATIVE_YANDEX_CTRL", message())
    }
}
