package com.l13devstudio.android.utills

import android.util.Log
import org.koin.java.KoinJavaComponent.inject
import com.l13devstudio.domain.entity.AdEnum
import com.l13devstudio.domain.entity.appConfig.AppConfigProvider
import kotlin.random.Random

fun AdEnum.isShow(): Boolean {
    val configProvider: AppConfigProvider by inject(AppConfigProvider::class.java)
    val config = configProvider.getConfig()
    val percent = when (this) {
        AdEnum.NATIVE -> config.percentShowNativeAd
        AdEnum.INTER -> config.percentShowInterAd
    }

    val randomValue = Random.nextInt(100)
    val isShowAd = randomValue < percent

    Log.d("CHANCE_SHOW_AD", "$this $percent% -> $isShowAd")

    return isShowAd
}