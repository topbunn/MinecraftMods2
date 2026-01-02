package com.hamit.data.repository

import com.hamit.data.api.CoreNetworkService
import com.hamit.data.api.dto.mods.toEntity
import com.hamit.data.database.dao.LikeDao
import com.hamit.data.database.entity.LikeEntity
import com.hamit.data.mappers.EntityTransformer
import com.hamit.data.storage.DataStoreStorage
import com.hamit.data.syncStream
import com.hamit.domain.entity.ProblemEntity
import com.hamit.domain.entity.PropertyEntity
import com.hamit.domain.entity.StorageKeys
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.domain.entity.addon.AddonSortType
import com.hamit.domain.entity.addon.AddonType
import com.hamit.domain.entity.addonConfig.AddonConfigProvider

class DataRepository(
    private val recordAccess: LikeDao,
    private val networkService: CoreNetworkService,
    private val transformer: EntityTransformer,
    private val dataStore: DataStoreStorage,
    private val configProvider: AddonConfigProvider
) {

    suspend fun getApps() = runCatching {
        networkService.fetchApplicationList().toEntity(configProvider.getConfig().applicationId)
    }

    suspend fun downloadFile(url: String, filename: String) = runCatching {
        networkService.streamResource(url).syncStream(filename)
    }

    suspend fun getMods(
        q: String,
        offset: Int,
        type: AddonType?,
        sortType: AddonSortType,
        limit: Int = 6,
    ) = runCatching {
        val response = networkService.fetchDataList(
            q = q,
            skip = offset,
            category = type,
            sortKey = sortType.toString(),
            take = limit,
            appId = configProvider.getConfig().appId
        )
        transformer.toEntity(response.items)
    }


    suspend fun getMod(id: Int) = runCatching {
        val mod = networkService.fetchDetails(id)
        transformer.toEntity(mod)
    }


    suspend fun getLikeTotalSize() = recordAccess.getActiveRecords().size

    suspend fun getFavoriteMods(offset: Int, limit: Int = 6) = runCatching {
        val favoriteIds = recordAccess.getActiveRecords()
            .drop(offset)
            .take(limit)
            .map { it.addonId }
        val mods = mutableListOf<AddonEntity>()
        favoriteIds.forEach {
            try {
                val mod = networkService.fetchDetails(it)
                mods.add(transformer.toEntity(mod))
            } catch (_: Exception) {
            }
        }
        return@runCatching mods
    }


    suspend fun addLike(record: LikeEntity) {
        val oldRecord = recordAccess.getRecord(record.addonId)
        recordAccess.saveRecord(record.copy(id = oldRecord?.id ?: 0))
    }

    suspend fun sendIssue(issue: ProblemEntity) = runCatching {
        networkService.reportIssue(
            issue = issue,
            id = configProvider.getConfig().appId
        )
    }

    private suspend fun loadConfig() = runCatching {
        val info = networkService.loadConfiguration(configProvider.getConfig().appId)
        info.coreParams?.let {
            dataStore.save(StorageKeys.AD_IS_ENABLED, it.isAdEnabled.toString())
            dataStore.save(StorageKeys.APPLOVIN_OPEN, it.applovinOpen)
            dataStore.save(StorageKeys.APPLOVIN_INTER, it.applovinInter)
            dataStore.save(StorageKeys.APPLOVIN_NATIVE, it.applovinNative)
            dataStore.save(StorageKeys.YANDEX_OPEN, it.yandexOpen)
            dataStore.save(StorageKeys.YANDEX_INTER, it.yandexInter)
            dataStore.save(StorageKeys.YANDEX_NATIVE, it.yandexNative)
        }
    }

    suspend fun receiveConfig(): PropertyEntity {
        loadConfig()

        val isAdEnabled = dataStore.get(StorageKeys.AD_IS_ENABLED, null)?.toBoolean() ?: true
        val applovinOpen = dataStore.get(StorageKeys.APPLOVIN_OPEN, null)
        val applovinInter = dataStore.get(StorageKeys.APPLOVIN_INTER, null)
        val applovinNative = dataStore.get(StorageKeys.APPLOVIN_NATIVE, null)
        val yandexOpen = dataStore.get(StorageKeys.YANDEX_OPEN, null)
        val yandexInter = dataStore.get(StorageKeys.YANDEX_INTER, null)
        val yandexNative = dataStore.get(StorageKeys.YANDEX_NATIVE, null)
        return PropertyEntity(
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