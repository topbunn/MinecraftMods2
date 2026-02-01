package com.l13devstudio.faq.di

import com.l13devstudio.faq.FaqViewModel
import org.koin.dsl.module

val faqFeatureModule = module {
    single { FaqViewModel() }
}