package com.hamit.domain.entity.storage

interface Storage {

    suspend fun save(key: StorageKeys, value: String)

    suspend fun get(key: StorageKeys, defaultValue: String?): String?

}