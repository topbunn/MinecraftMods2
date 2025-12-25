package com.hamit.data.di

import org.koin.dsl.module
import com.hamit.data.database.AppDatabase
import com.hamit.data.database.dao.RecordAccessObject

internal val databaseModule = module {
    single<AppDatabase>{ AppDatabase.getInstance(context = get()) }
    single<RecordAccessObject>{ get<AppDatabase>().recordAccess() }
}