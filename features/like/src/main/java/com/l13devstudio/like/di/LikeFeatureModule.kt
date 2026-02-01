package com.l13devstudio.like.di

import com.l13devstudio.like.LikeViewModel
import org.koin.dsl.module

val likeFeatureModule = module {
    single { LikeViewModel(get(), get()) }
}