package com.hamit.like.di

import com.hamit.like.LikeViewModel
import org.koin.dsl.module

val likeFeatureModule = module {
    single { LikeViewModel(get(), get()) }
}