package com.hamit.data.di

import org.koin.dsl.module
import com.hamit.data.api.ApiFactory
import com.hamit.data.api.LocationApi
import com.hamit.data.api.ModsApi

internal val apiModule = module {
    single<ModsApi>{ ApiFactory.modsApi }
    single<LocationApi>{ ApiFactory.locationApi }
}