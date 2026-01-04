package com.hamit.data.di

import com.hamit.data.source.remote.ApiFactory
import com.hamit.data.source.remote.api.AdConfigApi
import com.hamit.data.source.remote.api.AddonApi
import com.hamit.data.source.remote.api.FileApi
import com.hamit.data.source.remote.api.ProblemApi
import com.hamit.data.source.remote.api.RegionApi
import com.hamit.data.source.remote.api.SuggestApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val apiModule = module {
    singleOf(::ApiFactory)
    single<AdConfigApi> { AdConfigApi(get()) }
    single<AddonApi> { AddonApi(get()) }
    single<FileApi> { FileApi(get()) }
    single<ProblemApi> { ProblemApi(get()) }
    single<RegionApi> { RegionApi(get()) }
    single<SuggestApi> { SuggestApi(get()) }
}