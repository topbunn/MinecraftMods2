package com.hamit.app.app

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupSharedScreens()
        setupApplovin()
        setupYandex()
        setupFirebasePush()
        setupMetrics()
    }

}