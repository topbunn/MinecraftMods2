package com.hamit.addon.di

import com.hamit.addon.AddonViewModel
import com.hamit.addon.issue.IssueViewModel
import org.koin.dsl.module

val addonFeatureModule = module {
    factory { (addonId: Int) -> AddonViewModel(addonId, get(), get(), get()) }
    single { IssueViewModel() }
}