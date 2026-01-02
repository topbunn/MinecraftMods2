package com.hamit.data.di

import org.koin.dsl.module
import com.hamit.data.transforms.AddonTransformer

internal val mappersModule = module {
    factory<AddonTransformer> { AddonTransformer(context = get()) }
}