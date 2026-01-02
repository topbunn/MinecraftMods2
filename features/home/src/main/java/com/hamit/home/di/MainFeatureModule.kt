package com.hamit.home.di

import org.koin.dsl.module
import com.hamit.home.HomeViewModel

val mainFeatureModule = module {
    single { HomeViewModel(get(), get()) }
}