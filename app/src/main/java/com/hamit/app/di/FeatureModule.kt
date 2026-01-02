package com.hamit.app.di

import com.hamit.addon.di.addonFeatureModule
import com.hamit.home.di.mainFeatureModule
import com.hamit.like.di.likeFeatureModule
import com.hamit.loader.di.loaderFeatureModule
import org.koin.dsl.module

val featureModule = module {
    includes(
        addonFeatureModule,
        likeFeatureModule,
        mainFeatureModule,
        loaderFeatureModule,
    )
}