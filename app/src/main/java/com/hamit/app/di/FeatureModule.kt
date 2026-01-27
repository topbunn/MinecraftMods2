package com.hamit.app.di

import com.hamit.addon.di.addonFeatureModule
import com.hamit.download.di.downloadFeatureModule
import com.hamit.faq.di.faqFeatureModule
import com.hamit.home.di.mainFeatureModule
import com.hamit.issue.di.issueFeatureModule
import com.hamit.like.di.likeFeatureModule
import com.hamit.loader.di.loaderFeatureModule
import com.hamit.suggest.di.suggestFeatureModule
import org.koin.dsl.module

val featureModule = module {
    includes(
        addonFeatureModule,
        likeFeatureModule,
        mainFeatureModule,
        loaderFeatureModule,
        suggestFeatureModule,
        downloadFeatureModule,
        issueFeatureModule,
        faqFeatureModule,
    )
}