package com.hamit.issue.di

import com.hamit.issue.IssueViewModel
import org.koin.dsl.module

val issueFeatureModule = module {
    factory { IssueViewModel(get()) }
}