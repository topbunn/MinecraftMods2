package com.hamit.addon.di

import org.koin.dsl.module
import com.hamit.addon.AddonViewModel
import com.hamit.addon.dontWorkAddon.DontWorkAddonViewModel

val addonFeatureModule = module {
    factory { (modId: Int) -> AddonViewModel(modId, get()) }
    factory { DontWorkAddonViewModel(get()) }
}