package com.l13devstudio.data.di

import org.koin.dsl.module
import com.l13devstudio.data.transforms.AddonTransformer

internal val mappersModule = module {
    factory<AddonTransformer> { AddonTransformer(context = get()) }
}