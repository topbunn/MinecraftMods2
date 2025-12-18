package ru.topbun.android.ad.natives

import android.content.Context
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import kotlinx.coroutines.*
import ru.topbun.android.R
import kotlin.math.min
import kotlin.math.pow

object NativeApplovinController {

    private const val MAX_POOL = 5

    private lateinit var adLoader: MaxNativeAdLoader
    private val adPool = ArrayDeque<MaxNativeAdView>(MAX_POOL)

    private var isInitialized = false
    private var retryCount = 0
    private var pendingLoads = 0

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun init(context: Context, adUnitId: String) {
        log { "Init native ads, adUnitId=$adUnitId" }
        if (isInitialized) return
        isInitialized = true

        adLoader = MaxNativeAdLoader(adUnitId)
        adLoader.setNativeAdListener(object : MaxNativeAdListener() {

            override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, nativeAd: MaxAd) {
                retryCount = 0
                pendingLoads--

                if (nativeAdView != null) {
                    if (adPool.size < MAX_POOL) {
                        adPool.add(nativeAdView)
                        log { "NativeView added to pool (size=${adPool.size})" }
                    } else {
                        adLoader.destroy(nativeAd)
                        log { "Pool full — destroying extra ad" }
                    }
                }

                preloadNext(context.applicationContext)
            }

            override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                retryCount++
                pendingLoads--

                val delayMs = 2.0.pow(min(retryCount, 6)).toLong() * 1000
                log { "Native load failed: ${error.message}. Retry in $delayMs ms" }

                scope.launch {
                    delay(delayMs)
                    preloadNext(context.applicationContext)
                }
            }
        })
    }

    private fun preloadNext(context: Context) {
        scope.launch {
            if (adPool.size + pendingLoads >= MAX_POOL) {
                log { "Pool full, skip preload" }
                return@launch
            }

            pendingLoads++
            log { "Preloading native (pool=${adPool.size}, pending=$pendingLoads)" }

            val binder = MaxNativeAdViewBinder.Builder(R.layout.applovin_native_ad_container)
                .setTitleTextViewId(R.id.applovin_title)
                .setBodyTextViewId(R.id.applovin_body)
                .setAdvertiserTextViewId(R.id.applovin_advertiser)
                .setIconImageViewId(R.id.applovin_icon)
                .setMediaContentViewGroupId(R.id.applovin_media)
                .setOptionsContentViewGroupId(R.id.applovin_options)
                .setCallToActionButtonId(R.id.applovin_call_to_action)
                .build()

            val adView = MaxNativeAdView(binder, context)
            adLoader.loadAd(adView)
        }
    }

    fun preload(context: Context) {
        if (!::adLoader.isInitialized) {
            log { "Ad loader not initialized" }
            return
        }

        log { "Start native preload" }
        repeat(MAX_POOL) { preloadNext(context) }
    }

    fun pop(context: Context): MaxNativeAdView? {
        val view = adPool.removeFirstOrNull()
        if (view != null) {
            log { "Native popped (remaining=${adPool.size})" }
            preloadNext(context.applicationContext)
        } else {
            log { "Pool empty — returning null" }
        }
        return view
    }

    fun destroy() {
        log { "Destroy native ad controller" }
        scope.coroutineContext.cancel()
        adPool.clear()
        adLoader.setNativeAdListener(null)
    }

    private fun log(msg: () -> String) {
        Log.d("NATIVE_POOL_CTRL", msg())
    }
}
