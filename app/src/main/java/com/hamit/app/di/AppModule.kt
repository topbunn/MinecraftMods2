package com.hamit.app.di

import com.hamit.app.R
import org.koin.dsl.module
import com.hamit.data.di.dataModule
import com.hamit.domain.entity.AppLogoRes
import com.hamit.domain.entity.appConfig.AppConfigProvider
import com.hamit.app.AppAppConfigProvider

val appModule = module {
    single<AppConfigProvider> { AppAppConfigProvider() }
    single<AppLogoRes> { AppLogoRes(R.drawable.logo) }
    includes(
        dataModule,
        featureModule,
        useCaseModule,
    )
}