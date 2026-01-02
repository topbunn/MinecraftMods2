package com.hamit.data.di

import org.koin.dsl.module
import com.hamit.data.source.local.storage.AppDataStorage

internal val storageModule = module {
    single { AppDataStorage(get()) }
}