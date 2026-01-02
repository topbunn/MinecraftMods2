package com.hamit.domain.entity

interface Storage {

    suspend fun save(key: StorageKeys, value: String)

    suspend fun get(key: StorageKeys, defaultValue: String?): String?

}