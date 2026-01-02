package com.hamit.data.di

import com.hamit.data.api.RetrofitFactory
import com.hamit.data.api.CoreNetworkService
import com.hamit.data.api.RegionService
import org.koin.dsl.module

internal val apiModule = module {
    single<CoreNetworkService> { RetrofitFactory.coreNetworkService }
    single<RegionService> { RetrofitFactory.regionApi }
}