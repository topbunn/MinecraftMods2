package com.l13devstudio.app.app

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import com.l13devstudio.app.di.appModule

fun App.setupKoin() {
    startKoin {
        androidLogger()
        androidContext(this@setupKoin)
        modules(appModule)
    }
}
