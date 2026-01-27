package com.hamit.faq.di

import com.hamit.faq.FaqViewModel
import org.koin.dsl.module

val faqFeatureModule = module {
    single { FaqViewModel() }
}