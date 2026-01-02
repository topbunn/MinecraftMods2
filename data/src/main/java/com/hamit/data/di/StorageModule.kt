package com.hamit.data.di

import org.koin.dsl.module
import com.hamit.data.storage.DataStoreStorage

internal val storageModule = module {
    single { DataStoreStorage(get()) }
}