package com.l13devstudio.download.di

import com.l13devstudio.domain.entity.addon.AddonEntity
import com.l13devstudio.download.DownloadViewModel
import org.koin.dsl.module

val downloadFeatureModule = module {
    factory { (addon: AddonEntity) -> DownloadViewModel(addon, get(), get(), get(), get())  }
}