package com.hamit.home.di

import com.hamit.home.HomeViewModel
import org.koin.dsl.module

val mainFeatureModule = module {
    single { HomeViewModel(get()) }
}