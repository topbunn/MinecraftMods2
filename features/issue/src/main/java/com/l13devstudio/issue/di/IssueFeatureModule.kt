package com.l13devstudio.issue.di

import com.l13devstudio.issue.IssueViewModel
import org.koin.dsl.module

val issueFeatureModule = module {
    factory { IssueViewModel(get()) }
}