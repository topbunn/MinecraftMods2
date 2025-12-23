package com.hamit.main.di

import org.koin.dsl.module
import com.hamit.main.MainViewModel

val mainFeatureModule = module {
    single { MainViewModel(get()) }
}