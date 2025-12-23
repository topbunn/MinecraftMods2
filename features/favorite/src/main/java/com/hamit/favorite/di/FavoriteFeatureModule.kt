package com.hamit.favorite.di

import org.koin.dsl.module
import com.hamit.favorite.FavoriteViewModel

val favoriteFeatureModule = module {
    factory { FavoriteViewModel(get()) }
}