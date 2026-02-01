package com.l13devstudio.loader.di

import org.koin.dsl.module
import com.l13devstudio.loader.LoaderViewModel

val loaderFeatureModule = module {
    single { LoaderViewModel(get(), get(), get()) }
}