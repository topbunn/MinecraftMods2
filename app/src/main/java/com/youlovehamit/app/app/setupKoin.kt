package com.youlovehamit.app.app

import com.youlovehamit.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

fun App.setupKoin() {
    startKoin {
        androidLogger()
        androidContext(this@setupKoin)
        modules(appModule)
    }
}
