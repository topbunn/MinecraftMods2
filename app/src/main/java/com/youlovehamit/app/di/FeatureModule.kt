package com.youlovehamit.app.di

import org.koin.dsl.module
import com.hamit.apps.di.appsFeatureModule
import com.hamit.detail_mod.di.detailModFeatureModule
import com.hamit.favorite.di.favoriteFeatureModule
import com.hamit.main.di.mainFeatureModule
import com.hamit.splash.di.splashFeatureModule

val featureModule = module {
    includes(
        appsFeatureModule,
        detailModFeatureModule,
        favoriteFeatureModule,
        mainFeatureModule,
        splashFeatureModule,
    )
}