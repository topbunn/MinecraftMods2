package ru.topbun.android.utils

import android.util.Log
import org.koin.java.KoinJavaComponent.inject
import ru.topbun.domain.entity.AdType
import ru.topbun.domain.entity.AdType.*
import ru.topbun.domain.entity.modConfig.ModConfigProvider
import kotlin.random.Random

fun AdType.isShow(): Boolean {
    val configProvider: ModConfigProvider by inject(ModConfigProvider::class.java)
    val config = configProvider.getConfig()
    val percent = when (this) {
        NATIVE -> config.percentShowNativeAd
        INTER -> config.percentShowInterAd
    }

    val randomValue = Random.nextInt(100)
    val isShowAd = randomValue < percent

    Log.d("CHANCE_SHOW_AD", "$this $percent% -> $isShowAd")

    return isShowAd
}