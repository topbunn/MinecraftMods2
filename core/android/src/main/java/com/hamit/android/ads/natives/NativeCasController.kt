package com.hamit.android.ads.natives

import android.content.Context
import android.util.Log
import com.cleveradssolutions.sdk.AdContentInfo
import com.cleveradssolutions.sdk.nativead.AdChoicesPlacement
import com.cleveradssolutions.sdk.nativead.CASNativeLoader
import com.cleveradssolutions.sdk.nativead.NativeAdContent
import com.cleveradssolutions.sdk.nativead.NativeAdContentCallback
import com.hamit.android.ads.natives.NativeCoordinator.PreloadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.pow


object NativeCasController {

    private const val POOL_SIZE = 5
    private const val REFILL_THRESHOLD = 2
    private const val LOAD_TIMEOUT_MS = 12_000L

    private lateinit var appContext: Context
    private lateinit var casId: String

    private var loader: CASNativeLoader? = null
    private val loadedAds = ArrayDeque<NativeAdContent>(POOL_SIZE)

    private var initialized = false
    private var isDestroyed = false

    private var retryAttempt = 0
    private var loadStartedAt = 0L

    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var onPreloadComplete: ((PreloadStatus) -> Unit)? = null

    fun setCallback(callback: (PreloadStatus) -> Unit) {
        onPreloadComplete = callback
    }

    fun deleteCallback() {
        onPreloadComplete = null
    }

    fun init(context: Context, casId: String) {
        if (initialized) {
            log("Инициализация пропущена — уже инициализировано")
            return
        }

        this.appContext = context.applicationContext
        this.casId = casId

        initialized = true
        isDestroyed = false
        retryAttempt = 0
        loadStartedAt = 0L

        log("Инициализация CAS Native, casId=$casId")

        createLoader()
    }

    fun load() {
        if (!initialized) {
            log("Ошибка: load() вызван до init()")
            return
        }

        log("Старт предзагрузки нативной рекламы CAS")
        loadNext()
    }

    fun hasAd(): Boolean = loadedAds.isNotEmpty()

    fun pop(): NativeAdContent? {
        val ad = loadedAds.removeFirstOrNull()

        if (ad == null) {
            log("Попытка получить рекламу — пул пуст")
            maybeRefill()
            return null
        }

        log("Нативная реклама выдана из пула (осталось ${loadedAds.size})")
        maybeRefill()

        return ad
    }

    private fun maybeRefill() {
        if (!initialized || isDestroyed) return

        if (loadedAds.size > REFILL_THRESHOLD) {
            log("Пополнение не требуется (в пуле ${loadedAds.size})")
            return
        }

        if (loader?.isLoading == true) {
            log("Пополнение пула отложено — загрузка уже идёт")
            return
        }

        log("Пул ниже порога ($REFILL_THRESHOLD), запускаем загрузку")
        loadNext()
    }

    private fun loadNext() {
        if (!initialized || isDestroyed) return

        if (loadedAds.size >= POOL_SIZE) {
            log("Пул заполнен (${loadedAds.size}/$POOL_SIZE), загрузка не требуется")
            return
        }

        val now = System.currentTimeMillis()

        if (loader?.isLoading == true) {
            if (now - loadStartedAt > LOAD_TIMEOUT_MS) {
                log("Загрузка зависла — пересоздаём loader")
                recreateLoader()
            } else {
                log("Загрузка уже идёт, ожидаем callback")
                return
            }
        }

        loadStartedAt = now
        log("Запрос нативной рекламы (в пуле: ${loadedAds.size}/$POOL_SIZE)")
        loader?.load(1)
    }

    private fun recreateLoader() {
        createLoader()
        loadStartedAt = 0L
        loadNext()
    }

    private fun createLoader() {
        loader = CASNativeLoader(
            appContext,
            casId,
            nativeCallback
        ).apply {
            adChoicesPlacement = AdChoicesPlacement.TOP_LEFT
            isStartVideoMuted = true
        }
    }

    private fun preloadStatus(): PreloadStatus =
        if (loadedAds.size >= POOL_SIZE)
            PreloadStatus.PRELOADED
        else
            PreloadStatus.NOT_PRELOADED

    private val nativeCallback = object : NativeAdContentCallback() {

        override fun onNativeAdLoaded(
            nativeAd: NativeAdContent,
            ad: AdContentInfo
        ) {
            loadStartedAt = 0L
            retryAttempt = 0

            if (isDestroyed) {
                log("Реклама получена после destroy() — уничтожаем")
                nativeAd.destroy()
                return
            }

            if (loadedAds.size < POOL_SIZE) {
                loadedAds.add(nativeAd)
                log("Нативная реклама добавлена в пул (${loadedAds.size}/$POOL_SIZE)")
            } else {
                log("Пул переполнен — лишняя реклама уничтожена")
                nativeAd.destroy()
            }

            onPreloadComplete?.invoke(preloadStatus())
            loadNext()
        }

        override fun onNativeAdFailedToLoad(error: com.cleversolutions.ads.AdError) {
            loadStartedAt = 0L
            retryAttempt++

            val delayMs = 2.0.pow(min(retryAttempt, 6)).toLong() * 1000

            log("Ошибка загрузки нативной рекламы: ${error.message}. Повтор через $delayMs мс")

            scope.launch {
                delay(delayMs)
                loadNext()
            }
        }

        override fun onNativeAdFailedToShow(
            nativeAd: NativeAdContent,
            error: com.cleversolutions.ads.AdError
        ) {
            log("Ошибка показа нативной рекламы: ${error.message}")
            nativeAd.destroy()
        }

        override fun onNativeAdClicked(
            nativeAd: NativeAdContent,
            ad: AdContentInfo
        ) {
            log("Клик по нативной рекламе")
        }
    }

    fun destroy() {
        log("Уничтожение CAS Native Controller")

        isDestroyed = true

        scope.cancel()
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        loadedAds.forEach { it.destroy() }
        loadedAds.clear()

        loader = null
        initialized = false
        retryAttempt = 0
        loadStartedAt = 0L
    }

    private fun log(message: String) {
        Log.d("CAS_NATIVE_AD", message)
    }
}