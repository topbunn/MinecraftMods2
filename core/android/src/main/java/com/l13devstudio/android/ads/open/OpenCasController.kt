package com.l13devstudio.android.ads.open

import android.app.Activity
import android.content.Context
import android.util.Log
import com.cleveradssolutions.sdk.AdContentInfo
import com.cleveradssolutions.sdk.AdFormat
import com.cleveradssolutions.sdk.screen.CASAppOpen
import com.cleveradssolutions.sdk.screen.ScreenAdContentCallback
import com.cleversolutions.ads.AdError
import kotlin.math.min
import kotlin.math.pow

object OpenCasController {

    private var appOpenAd: CASAppOpen? = null

    private var initialized = false
    private var state = AdState.IDLE
    private var retryAttempt = 0

    private enum class AdState {
        IDLE,
        LOADING,
        READY,
        SHOWING
    }

    fun init(context: Context, casId: String) {
        if (initialized) {
            log("Инициализация пропущена — уже инициализировано")
            return
        }

        initialized = true
        retryAttempt = 0
        state = AdState.IDLE

        log("Инициализация CAS App Open, casId=$casId")

        appOpenAd = CASAppOpen(casId).apply {
            contentCallback = this@OpenCasController.contentCallback
            isAutoloadEnabled = true
            isAutoshowEnabled = false
        }
        load(context)
    }

    fun load(context: Context) {
        if (!initialized || appOpenAd == null) {
            log("Ошибка: load() вызван до init()")
            return
        }

        if (state == AdState.LOADING) {
            log("Загрузка уже идёт — пропуск")
            return
        }

        log("Запрос загрузки App Open рекламы")
        state = AdState.LOADING
        appOpenAd?.load(context)
    }

    fun show(activity: Activity) {
        if (!initialized || appOpenAd == null) {
            log("Ошибка: show() вызван до init()")
            return
        }

        log("Попытка показа App Open рекламы. state=$state")

        if (state == AdState.READY && appOpenAd?.isLoaded == true) {
            log("Показываем App Open рекламу")
            state = AdState.SHOWING
            appOpenAd?.show(activity)
        } else {
            log("Реклама не готова — запускаем загрузку")
            load(activity.applicationContext)
        }
    }

    fun destroy() {
        log("Уничтожение CAS App Open Controller")

        appOpenAd?.destroy()
        appOpenAd = null

        initialized = false
        retryAttempt = 0
        state = AdState.IDLE
    }

    private val contentCallback = object : ScreenAdContentCallback() {

        override fun onAdLoaded(ad: AdContentInfo) {
            log("App Open реклама успешно загружена")
            retryAttempt = 0
            state = AdState.READY
        }

        override fun onAdFailedToLoad(format: AdFormat, error: AdError) {
            retryAttempt++
            state = AdState.IDLE

            val delay = calculateBackoff(retryAttempt)
            log("Ошибка загрузки App Open: ${error.message}. Рекомендуемая пауза $delay мс")
        }

        override fun onAdShowed(ad: AdContentInfo) {
            log("App Open реклама показана")
            state = AdState.SHOWING
        }

        override fun onAdDismissed(ad: AdContentInfo) {
            log("App Open реклама закрыта пользователем")
            state = AdState.IDLE
        }

        override fun onAdFailedToShow(format: AdFormat, error: AdError) {
            log("Ошибка показа App Open: ${error.message}")
            state = AdState.IDLE
        }

        override fun onAdClicked(ad: AdContentInfo) {
            log("Клик по App Open рекламе")
        }
    }

    private fun calculateBackoff(attempt: Int): Long {
        val exp = min(6, attempt)
        return (2.0.pow(exp.toDouble()) * 1000L).toLong()
    }

    private fun log(message: String) {
        Log.d("CAS_OPEN_AD", message)
    }
}
