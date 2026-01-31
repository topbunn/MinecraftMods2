package com.hamit.download.di

import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.download.DownloadViewModel
import org.koin.dsl.module

val downloadFeatureModule = module {
    factory { (addon: AddonEntity) -> DownloadViewModel(addon, get(), get(), get(), get())  }
}