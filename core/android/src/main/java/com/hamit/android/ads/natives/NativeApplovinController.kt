//package com.hamit.android.ads.natives
//
//import android.content.Context
//import android.util.Log
//import com.applovin.mediation.MaxAd
//import com.applovin.mediation.MaxError
//import com.applovin.mediation.nativeAds.MaxNativeAdListener
//import com.applovin.mediation.nativeAds.MaxNativeAdLoader
//import com.applovin.mediation.nativeAds.MaxNativeAdView
//import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
//import com.hamit.android.R
//import com.hamit.android.ads.natives.NativeCoordinator.PreloadStatus
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import kotlin.math.min
//import kotlin.math.pow
//
//object NativeApplovinController {
//
//    private const val POOL_SIZE = 5
//
//    private lateinit var adLoader: MaxNativeAdLoader
//    private val loadedAds = ArrayDeque<MaxAd>(POOL_SIZE)
//
//    private var initialized = false
//    private var retryAttempt = 0
//    private var loadingCount = 0
//
//    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
//
//    private var onPreloadComplete: ((PreloadStatus) -> Unit)? = null
//
//    fun setCallback(callback: (PreloadStatus) -> Unit) {
//        onPreloadComplete = callback
//    }
//
//    fun deleteCallback() {
//        onPreloadComplete = null
//    }
//
//    fun init(context: Context, adUnitId: String) {
//        log { "Инициализация Native Ad с adUnitId=$adUnitId" }
//        if (initialized) return
//        initialized = true
//
//        adLoader = MaxNativeAdLoader(adUnitId, context.applicationContext)
//        adLoader.setNativeAdListener(object : MaxNativeAdListener() {
//
//            override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, nativeAd: MaxAd) {
//                retryAttempt = 0
//                loadingCount--
//
//                if (loadedAds.size < POOL_SIZE) {
//                    loadedAds.add(nativeAd)
//                    log { "Native Ad добавлена в пул (текущий размер: ${loadedAds.size})" }
//                } else {
//                    adLoader.destroy(nativeAd)
//                    log { "Пул переполнен, лишняя реклама уничтожена" }
//                }
//
//                loadNext()
//                onPreloadComplete?.invoke(isPreloadComplete())
//            }
//
//            override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
//                retryAttempt++
//                loadingCount--
//                val delayMs = 2.0.pow(min(retryAttempt, 6)).toLong() * 1000
//                log { "Ошибка загрузки Native Ad: ${error.message}. Повтор через $delayMs ms" }
//
//                scope.launch {
//                    delay(delayMs)
//                    loadNext()
//                }
//            }
//        })
//    }
//
//
//    fun hasAd() = loadedAds.isNotEmpty()
//
//    private fun isPreloadComplete() =
//        if (loadedAds.size >= POOL_SIZE) PreloadStatus.PRELOADED else PreloadStatus.NOT_PRELOADED
//
//    private fun loadNext() {
//        scope.launch {
//            if (loadedAds.size + loadingCount >= POOL_SIZE) {
//                log { "Пул полон, загрузка отменена" }
//                return@launch
//            }
//
//            loadingCount++
//            log { "Запуск загрузки Native Ad (пул: ${loadedAds.size}, в процессе: $loadingCount)" }
//
//            adLoader.loadAd()
//        }
//    }
//
//    fun load(context: Context) {
//        if (!::adLoader.isInitialized) {
//            log { "Ошибка: вызов load() до инициализации" }
//            return
//        }
//        log { "Старт предзагрузки пула Native Ads" }
//        repeat(POOL_SIZE) { loadNext() }
//    }
//
//
//    fun pop(context: Context, layoutResId: Int): MaxNativeAdView? {
//        val nativeAd = loadedAds.removeFirstOrNull() ?: run {
//            log { "Нет готовых объявлений в пуле" }
//            loadNext()
//            return null
//        }
//
//        log { "Реклама взята из пула. Осталось: ${loadedAds.size}" }
//
//        val binder = MaxNativeAdViewBinder.Builder(layoutResId)
//            .setTitleTextViewId(R.id.applovin_title)
//            .setBodyTextViewId(R.id.applovin_body)
//            .setAdvertiserTextViewId(R.id.applovin_advertiser)
//            .setIconImageViewId(R.id.applovin_icon)
//            .setMediaContentViewGroupId(R.id.applovin_media)
//            .setOptionsContentViewGroupId(R.id.applovin_options)
//            .setCallToActionButtonId(R.id.applovin_call_to_action)
//            .build()
//
//        val adView = MaxNativeAdView(binder, context)
//
//        adLoader.render(adView, nativeAd)
//
//        loadNext()
//
//        return adView
//    }
//
//    fun destroy() {
//        log { "Destroy Native Ad Manager" }
//
//        scope.cancel()
//        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
//
//        loadedAds.forEach { adLoader.destroy(it) }
//        loadedAds.clear()
//
//        if (::adLoader.isInitialized) {
//            adLoader.destroy()
//        }
//        initialized = false
//    }
//
//    private fun log(message: () -> String) = Log.d("APPLOVIN_NATIVE_AD", message())
//}