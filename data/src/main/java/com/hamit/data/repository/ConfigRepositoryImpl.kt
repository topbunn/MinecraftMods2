package com.hamit.data.repository

import com.hamit.data.source.local.storage.AppDataStorage
import com.hamit.data.source.remote.api.AdConfigApi
import com.hamit.data.source.remote.dto.adConfig.AdConfigResponse
import com.hamit.domain.entity.adConfig.AdConfigEntity
import com.hamit.domain.entity.appConfig.AppConfigProvider
import com.hamit.domain.entity.storage.StorageKeys
import com.hamit.domain.repository.ConfigRepository
import io.ktor.client.call.body

class ConfigRepositoryImpl(
    private val dataStore: AppDataStorage,
    private val configApi: AdConfigApi,
    private val configProvider: AppConfigProvider
) : ConfigRepository {

    private suspend fun loadConfig() {
        try {
            val info = configApi.loadConfiguration(configProvider.getConfig().appId).body<AdConfigResponse>()
            info.sdk?.let {
                dataStore.save(StorageKeys.AD_IS_ENABLED, it.isAdEnabled.toString())
                dataStore.save(StorageKeys.APPLOVIN_OPEN, it.applovinOpen)
                dataStore.save(StorageKeys.APPLOVIN_INTER, it.applovinInter)
                dataStore.save(StorageKeys.APPLOVIN_NATIVE, it.applovinNative)
                dataStore.save(StorageKeys.YANDEX_OPEN, it.yandexOpen)
                dataStore.save(StorageKeys.YANDEX_INTER, it.yandexInter)
                dataStore.save(StorageKeys.YANDEX_NATIVE, it.yandexNative)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun receiveConfig(): AdConfigEntity {
        loadConfig()

        val isAdEnabled = dataStore.get(StorageKeys.AD_IS_ENABLED, null)?.toBoolean() ?: true
        val applovinOpen = dataStore.get(StorageKeys.APPLOVIN_OPEN, null)
        val applovinInter = dataStore.get(StorageKeys.APPLOVIN_INTER, null)
        val applovinNative = dataStore.get(StorageKeys.APPLOVIN_NATIVE, null)
        val yandexOpen = dataStore.get(StorageKeys.YANDEX_OPEN, null)
        val yandexInter = dataStore.get(StorageKeys.YANDEX_INTER, null)
        val yandexNative = dataStore.get(StorageKeys.YANDEX_NATIVE, null)
        return AdConfigEntity(
            isAdEnabled,
            applovinOpen,
            applovinInter,
            applovinNative,
            yandexOpen,
            yandexInter,
            yandexNative
        )
    }

}