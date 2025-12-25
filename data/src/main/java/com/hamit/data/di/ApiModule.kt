package com.hamit.data.di

import com.hamit.data.api.ApiFactory
import com.hamit.data.api.CoreNetworkService
import com.hamit.data.api.RegionService
import org.koin.dsl.module

internal val apiModule = module {
    single<CoreNetworkService> { ApiFactory.coreNetworkService }
    single<RegionService> { ApiFactory.regionApi }
}