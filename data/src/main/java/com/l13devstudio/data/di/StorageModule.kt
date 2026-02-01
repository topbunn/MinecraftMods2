package com.l13devstudio.data.di

import org.koin.dsl.module
import com.l13devstudio.data.source.local.storage.AppDataStorage

internal val storageModule = module {
    single { AppDataStorage(get()) }
}