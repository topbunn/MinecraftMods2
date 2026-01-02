package com.hamit.data.di

import org.koin.dsl.module
import com.hamit.data.mappers.EntityTransformer

internal val mappersModule = module {
    factory<EntityTransformer> { EntityTransformer(context = get()) }
}