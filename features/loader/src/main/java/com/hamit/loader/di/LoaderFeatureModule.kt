package com.hamit.loader.di

import org.koin.dsl.module
import com.hamit.loader.LoaderViewModel

val loaderFeatureModule = module {
    single { LoaderViewModel(get(), get(), get()) }
}