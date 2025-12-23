package com.youlovehamit.app.di

import com.youlovehamit.app.AppAddonConfigProvider
import com.youlovehamit.app.R
import org.koin.dsl.module
import com.hamit.data.di.dataModule
import com.hamit.domain.entity.AppLogoRes
import com.hamit.domain.entity.addonConfig.AddonConfigProvider

val appModule = module {
    single<AddonConfigProvider> { AppAddonConfigProvider() }
    single<AppLogoRes> { AppLogoRes(R.drawable.logo) }
    includes(
        dataModule,
        featureModule
    )
}