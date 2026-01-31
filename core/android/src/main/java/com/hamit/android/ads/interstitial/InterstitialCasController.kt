package com.hamit.android.ads.interstitial

import android.app.Activity
import android.content.Context
import android.util.Log
import com.cleveradssolutions.sdk.AdContentInfo
import com.cleveradssolutions.sdk.AdFormat
import com.cleveradssolutions.sdk.screen.CASInterstitial
import com.cleveradssolutions.sdk.screen.ScreenAdContentCallback
import com.cleversolutions.ads.AdError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.pow

object InterstitialCasController {

    private var interstitialAd: CASInterstitial? = null

    private var initialized = false

    @Volatile
    private var paused = false

    @Volatile
    private var state = AdState.IDLE

    private var retryAttempt = 0

    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var retryJob: Job? = null

    private var onAdReady: (() -> Unit)? = null

    private enum class AdState {
        IDLE,
        LOADING,
        READY,
        SHOWING
    }

    fun setCallback(callback: () -> Unit) {
        onAdReady = callback
    }

    fun deleteCallback() {
        onAdReady = null
    }

    fun init(context: Context, casId: String) {
        if (initialized) {
            log("Инициализация пропущена — уже инициализировано")
            return
        }

        log("Инициализация CAS Interstitial, casId=$casId")

        initialized = true
        paused = false
        retryAttempt = 0
        state = AdState.IDLE

        interstitialAd = CASInterstitial(casId).apply {
            contentCallback = this@InterstitialCasController.contentCallback
            isAutoloadEnabled = true
            isAutoshowEnabled = false
        }
        load(context)
    }

    fun load(context: Context) {
        if (!initialized || interstitialAd == null) {
            log("Ошибка: load() вызван до init()")
            return
        }

        if (paused) {
            log("Загрузка отменена — менеджер в паузе")
            return
        }

        if (state == AdState.LOADING || state == AdState.SHOWING) {
            log("Загрузка уже идёт — state=$state")
            return
        }

        if (interstitialAd?.isLoaded == true) {
            log("Реклама уже загружена — повторная загрузка не требуется")
            return
        }

        log("Началась загрузка CAS Interstitial")
        state = AdState.LOADING

        retryJob?.cancel()
        interstitialAd?.load(context.applicationContext)
    }

    fun show(activity: Activity) {
        if (!initialized || interstitialAd == null) {
            log("Ошибка: show() вызван до init()")
            return
        }

        log("Попытка показать CAS Interstitial. state=$state")

        if (state == AdState.READY && interstitialAd?.isLoaded == true) {
            log("CAS Interstitial готова — показываем")
            state = AdState.SHOWING
            interstitialAd?.show(activity)
        } else {
            log("CAS Interstitial не готова")
        }
    }

    fun stop() {
        log("PAUSE — ставим paused=true и отменяем ретраи")
        paused = true
        retryJob?.cancel()
    }

    fun start(context: Context) {
        log("RESUME — снимаем paused и проверяем загрузку")
        paused = false

        if (interstitialAd?.isLoaded != true) {
            load(context)
        }
    }

    fun destroy() {
        log("Destroy — очищаем CAS Interstitial Controller")

        paused = true
        retryJob?.cancel()

        interstitialAd?.destroy()
        interstitialAd = null

        scope.cancel()
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        initialized = false
        retryAttempt = 0
        state = AdState.IDLE
    }

    private val contentCallback = object : ScreenAdContentCallback() {

        override fun onAdLoaded(ad: AdContentInfo) {
            log("CAS Interstitial успешно загружена")
            retryAttempt = 0
            state = AdState.READY
            onAdReady?.invoke()
        }

        override fun onAdFailedToLoad(format: AdFormat, error: AdError) {
            retryAttempt++
            state = AdState.IDLE

            val delayMs = calculateBackoff(retryAttempt)
            log("Ошибка загрузки CAS Interstitial: ${error.message}. Повтор через $delayMs мс")

            retryJob?.cancel()
            retryJob = scope.launch {
                delay(delayMs)
                if (!paused) {
                    interstitialAd?.load(null)
                }
            }
        }

        override fun onAdShowed(ad: AdContentInfo) {
            log("CAS Interstitial показана")
            state = AdState.SHOWING
        }

        override fun onAdDismissed(ad: AdContentInfo) {
            log("CAS Interstitial закрыта пользователем")
            state = AdState.IDLE

            if (!paused) {
                interstitialAd?.load(null)
            } else {
                log("Не загружаем — менеджер в паузе")
            }
        }

        override fun onAdFailedToShow(format: AdFormat, error: AdError) {
            log("Ошибка показа CAS Interstitial: ${error.message}")
            state = AdState.IDLE

            if (!paused) {
                interstitialAd?.load(null)
            }
        }

        override fun onAdClicked(ad: AdContentInfo) {
            log("CAS Interstitial кликнута")
        }
    }

    private fun calculateBackoff(attempt: Int): Long {
        val exp = min(6, attempt)
        return (2.0.pow(exp.toDouble()) * 1000L).toLong()
    }

    private fun log(message: String) {
        Log.d("CAS_INTER_AD", message)
    }
}
