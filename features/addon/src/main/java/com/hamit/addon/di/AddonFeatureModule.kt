package com.hamit.addon.di

import org.koin.dsl.module
import com.hamit.addon.AddonViewModel
import com.hamit.addon.dontWorkAddon.DontWorkAddonViewModel

val addonFeatureModule = module {
    factory { (addonId: Int) -> AddonViewModel(addonId, get(), get()) }
    factory { DontWorkAddonViewModel(get()) }
}