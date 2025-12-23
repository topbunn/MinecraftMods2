package com.hamit.apps.di

import org.koin.dsl.module
import com.hamit.apps.AppsViewModel

val appsFeatureModule = module {
    single { AppsViewModel(get(), get()) }
}