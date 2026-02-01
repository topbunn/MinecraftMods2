package com.l13devstudio.suggest.di

import com.l13devstudio.suggest.SuggestViewModel
import org.koin.dsl.module

val suggestFeatureModule = module {
    single { SuggestViewModel(get()) }
}