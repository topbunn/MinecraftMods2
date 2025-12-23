package com.youlovehamit.app.di

import com.youlovehamit.app.AppModConfigProvider
import com.youlovehamit.app.R
import org.koin.dsl.module
import com.hamit.data.di.dataModule
import com.hamit.domain.entity.LogoAppRes
import com.hamit.domain.entity.modConfig.ModConfigProvider

val appModule = module {
    single<ModConfigProvider> { AppModConfigProvider() }
    single<LogoAppRes> { LogoAppRes(R.drawable.logo) }
    includes(
        dataModule,
        featureModule
    )
}