package com.l13devstudio.app.di

import com.l13devstudio.addon.di.addonFeatureModule
import com.l13devstudio.download.di.downloadFeatureModule
import com.l13devstudio.faq.di.faqFeatureModule
import com.l13devstudio.home.di.mainFeatureModule
import com.l13devstudio.issue.di.issueFeatureModule
import com.l13devstudio.like.di.likeFeatureModule
import com.l13devstudio.loader.di.loaderFeatureModule
import com.l13devstudio.suggest.di.suggestFeatureModule
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