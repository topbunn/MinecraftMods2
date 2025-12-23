package com.hamit.android.utils

import android.util.Log
import org.koin.java.KoinJavaComponent.inject
import com.hamit.domain.entity.AdEnum
import com.hamit.domain.entity.AdEnum.*
import com.hamit.domain.entity.addonConfig.AddonConfigProvider
import kotlin.random.Random

fun AdEnum.isShow(): Boolean {
    val configProvider: AddonConfigProvider by inject(AddonConfigProvider::class.java)
    val config = configProvider.getConfig()
    val percent = when (this) {
        com.hamit.domain.entity.NATIVE -> config.percentShowNativeAd
        com.hamit.domain.entity.INTER -> config.percentShowInterAd
    }

    val randomValue = Random.nextInt(100)
    val isShowAd = randomValue < percent

    Log.d("CHANCE_SHOW_AD", "$this $percent% -> $isShowAd")

    return isShowAd
}