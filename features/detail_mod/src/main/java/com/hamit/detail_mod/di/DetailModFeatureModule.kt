package com.hamit.detail_mod.di

import org.koin.dsl.module
import com.hamit.detail_mod.DetailModViewModel
import com.hamit.detail_mod.dontWorkAddon.DontWorkAddonViewModel

val detailModFeatureModule = module {
    factory{ (modId: Int) -> DetailModViewModel(modId, get()) }
    factory {  DontWorkAddonViewModel(get()) }
}