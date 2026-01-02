package com.hamit.data.di

import com.hamit.data.repository.DataRepository
import com.hamit.data.repository.RegionProvider
import org.koin.dsl.module

internal val repositoryModule = module {
    single {
        DataRepository(
            recordAccess = get(),
            networkService = get(),
            transformer = get(),
            dataStore = get(),
            configProvider = get()
        )
    }
    single {
        RegionProvider(
            context = get(),
            service = get()
        )
    }
}