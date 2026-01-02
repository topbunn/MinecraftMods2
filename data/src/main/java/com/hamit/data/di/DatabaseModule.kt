package com.hamit.data.di

import org.koin.dsl.module
import com.hamit.data.source.local.database.CoreDatabase
import com.hamit.data.source.local.database.dao.LikeDao

internal val databaseModule = module {
    single<CoreDatabase>{ CoreDatabase.getInstance(context = get()) }
    single<LikeDao>{ get<CoreDatabase>().likeDao() }
}