package com.l13devstudio.android.ads.natives

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import com.l13devstudio.android.R
import com.l13devstudio.android.ads.natives.NativeCoordinator.PreloadStatus
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdEventListener
import com.yandex.mobile.ads.nativeads.NativeAdException
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration
import com.yandex.mobile.ads.nativeads.NativeAdView
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.pow

object NativeYandexController {

    private const val POOL_SIZE = 5

    private var adLoader: NativeAdLoader? = null
    private val loadedAds = ArrayDeque<NativeAd>(POOL_SIZE)

    private var adUnitId: String? = null
    private var initialized = false
    private var retryAttempt = 0
    private var loadingCount = 0

    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var onPreloadComplete: ((PreloadStatus) -> Unit)? = null

    fun setCallback(callback: (PreloadStatus) -> Unit) {
        onPreloadComplete = callback
    }

    fun deleteCallback() {
        onPreloadComplete = null
    }

    fun init(context: Context, adUnitId: String) {
        if (initialized) return
        initialized = true
        this.adUnitId = adUnitId

        log { "Инициализация Yandex Native Ad: $adUnitId" }

        adLoader = NativeAdLoader(context.applicationContext).apply {
            setNativeAdLoadListener(object : NativeAdLoadListener {

                override fun onAdLoaded(ad: NativeAd) {
                    retryAttempt = 0
                    loadingCount--

                    if (loadedAds.size < POOL_SIZE) {
                        loadedAds.add(ad)
                        log { "NativeAd добавлен в пул (${loadedAds.size})" }
                    } else {
                        log { "Пул переполнен, NativeAd пропущена" }
                    }

                    loadNext()
                    onPreloadComplete?.invoke(preloadStatus())
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    retryAttempt++
                    loadingCount--

                    val delayMs = 2.0.pow(min(retryAttempt, 6)).toLong() * 1000
                    log { "Ошибка загрузки: ${error.description}. Повтор через $delayMs ms" }

                    scope.launch {
                        delay(delayMs)
                        loadNext()
                    }
                }
            })
        }
    }

    fun hasAd() = loadedAds.isNotEmpty()

    private fun preloadStatus() =
        if (loadedAds.size >= POOL_SIZE) PreloadStatus.PRELOADED
        else PreloadStatus.NOT_PRELOADED

    private fun loadNext() {
        if (!initialized) return
        if (loadedAds.size + loadingCount >= POOL_SIZE) return

        loadingCount++
        log { "Запуск загрузки NativeAd (пул=${loadedAds.size}, loading=$loadingCount)" }

        adUnitId?.let {
            val request = NativeAdRequestConfiguration.Builder(it).build()
            adLoader?.loadAd(request)
        }
    }

    fun load() {
        log { "Старт предзагрузки Yandex Native Ads" }
        repeat(POOL_SIZE) { loadNext() }
    }

    fun pop(context: Context, layoutResId: Int): NativeAdView? {
        val ad = loadedAds.removeFirstOrNull() ?: run {
            log { "Пул пуст" }
            loadNext()
            return null
        }

        log { "NativeAd выдан из пула (осталось ${loadedAds.size})" }

        val adView = LayoutInflater.from(context)
            .inflate(layoutResId, null) as NativeAdView

        bind(ad, adView)
        loadNext()

        return adView
    }

    private fun bind(ad: NativeAd, adView: NativeAdView) {
        val binder = NativeAdViewBinder.Builder(adView)
            .setTitleView(adView.findViewById(R.id.yandex_title))
            .setBodyView(adView.findViewById(R.id.yandex_body))
            .setIconView(adView.findViewById(R.id.yandex_icon))
            .setMediaView(adView.findViewById(R.id.yandex_media))
            .setCallToActionView(adView.findViewById(R.id.yandex_call_to_action))
            .setDomainView(adView.findViewById(R.id.yandex_domain))
            .setSponsoredView(adView.findViewById(R.id.yandex_sponsored))
            .setWarningView(adView.findViewById(R.id.yandex_warning))
            .setFeedbackView(adView.findViewById(R.id.yandex_feedback))
            .setPriceView(adView.findViewById(R.id.yandex_price))
            .build()

        try {
            ad.bindNativeAd(binder)
            ad.setNativeAdEventListener(object : NativeAdEventListener {
                override fun onAdClicked() {
                    log { "Ad clicked" }
                }

                override fun onImpression(data: ImpressionData?) {
                    log { "Ad impression" }
                }

                override fun onLeftApplication() {}
                override fun onReturnedToApplication() {}
            })
        } catch (e: NativeAdException) {
            log { "Ошибка биндинга NativeAd: ${e.message}" }
        }
    }

    fun destroy() {
        log { "Destroy Yandex Native Ad Manager" }

        scope.cancel()
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        loadedAds.clear()

        adLoader = null
        initialized = false
        retryAttempt = 0
        loadingCount = 0
    }

    private fun log(message: () -> String) =
        Log.d("YANDEX_NATIVE_AD", message())
}