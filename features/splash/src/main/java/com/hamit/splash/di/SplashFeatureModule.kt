package com.hamit.splash.di

import org.koin.dsl.module
import com.hamit.splash.SplashViewModel

val splashFeatureModule = module {
    single { SplashViewModel(get(), get(), get()) }
}