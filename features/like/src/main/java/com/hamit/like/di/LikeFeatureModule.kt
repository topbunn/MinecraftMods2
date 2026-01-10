package com.hamit.like.di

import com.hamit.like.LikeViewModel
import org.koin.dsl.module

val likeFeatureModule = module {
    factory { LikeViewModel(get(), get()) }
}