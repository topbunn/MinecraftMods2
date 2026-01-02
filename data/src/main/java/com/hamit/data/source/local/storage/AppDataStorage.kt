package com.hamit.data.source.local.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import com.hamit.domain.entity.storage.Storage
import com.hamit.domain.entity.storage.StorageKeys

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "storage")

class AppDataStorage(private val context: Context) : Storage {

    override suspend fun save(key: StorageKeys, value: String) {
        context.dataStore.edit {
            it[stringPreferencesKey(key.toString())] = value
        }
    }

    override suspend fun get(key: StorageKeys, defaultValue: String?): String? {
        return context.dataStore.data.map {
            it[stringPreferencesKey(key.toString())] ?: defaultValue
        }.first()
    }

}