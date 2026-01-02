package com.hamit.domain.entity.appConfig

interface AppConfigProvider{
    fun getConfig(): AppConfig
}