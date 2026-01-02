package com.hamit.app.di

import com.hamit.app.R
import org.koin.dsl.module
import com.hamit.data.di.dataModule
import com.hamit.domain.entity.AppLogoRes
import com.hamit.domain.entity.addonConfig.AddonConfigProvider
import com.hamit.app.AppAddonConfigProvider

val appModule = module {
    single<AddonConfigProvider> { AppAddonConfigProvider() }
    single<AppLogoRes> { AppLogoRes(R.drawable.logo) }
    includes(
        dataModule,
        featureModule
    )
}