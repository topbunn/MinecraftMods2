package com.hamit.suggest.di

import com.hamit.suggest.SuggestViewModel
import org.koin.dsl.module

val suggestFeatureModule = module {
    single { SuggestViewModel(get()) }
}