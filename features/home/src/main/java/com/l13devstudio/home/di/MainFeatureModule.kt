package com.l13devstudio.home.di

import com.l13devstudio.home.HomeViewModel
import org.koin.dsl.module

val mainFeatureModule = module {
    single { HomeViewModel(get()) }
}