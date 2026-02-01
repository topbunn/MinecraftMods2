package com.l13devstudio.data.di

import org.koin.dsl.module

val dataModule = module {
    includes(
        apiModule,
        databaseModule,
        mappersModule,
        repositoryModule,
        storageModule,
    )
}