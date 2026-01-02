package com.hamit.like.di

import org.koin.dsl.module
import com.hamit.like.LikeViewModel

val likeFeatureModule = module {
    factory { LikeViewModel(get(), get(), get()) }
}