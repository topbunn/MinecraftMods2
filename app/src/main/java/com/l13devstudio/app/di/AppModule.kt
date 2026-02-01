package com.l13devstudio.app.di

import com.l13devstudio.app.R
import org.koin.dsl.module
import com.l13devstudio.data.di.dataModule
import com.l13devstudio.domain.entity.AppLogoRes
import com.l13devstudio.domain.entity.appConfig.AppConfigProvider
import com.l13devstudio.app.AppAppConfigProvider

val appModule = module {
    single<AppConfigProvider> { AppAppConfigProvider() }
    single<AppLogoRes> { AppLogoRes(R.drawable.logo) }
    includes(
        dataModule,
        featureModule,
        useCaseModule,
    )
}