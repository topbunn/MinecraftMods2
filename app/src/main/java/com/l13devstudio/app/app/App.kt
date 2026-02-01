package com.l13devstudio.app.app

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupSharedScreens()
//        setupApplovin()
        setupCas()
        setupYandex()
        setupFirebasePush()
        setupMetrics()
    }

}