package com.youlovehamit.app.app

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupSharedScreens()
        setupAppLovin()
        setupYandex()
        setupFirebasePush()
        setupMetrics()
    }

}