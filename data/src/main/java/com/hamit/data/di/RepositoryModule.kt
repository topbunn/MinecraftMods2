package com.hamit.data.di

import org.koin.dsl.module
import com.hamit.data.repository.LocationRepository
import com.hamit.data.repository.ModRepository

internal val repositoryModule = module {
    single {
        ModRepository(
            favoriteDao = get(),
            api = get(),
            modMapper = get(),
            dataStore = get(),
            configProvider = get()
        )
    }
    single {
        LocationRepository(
            context = get(),
            api = get()
        )
    }
}