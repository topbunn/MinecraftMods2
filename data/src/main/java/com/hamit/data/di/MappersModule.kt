package com.hamit.data.di

import org.koin.dsl.module
import com.hamit.data.mappers.ModMapper

internal val mappersModule = module {
    factory<ModMapper> { ModMapper(context = get()) }
}