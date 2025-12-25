package com.hamit.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hamit.data.database.entity.LocalRecord

@Dao
interface RecordAccessObject {

    @Query("SELECT * FROM local_records WHERE isActive = 1")
    suspend fun getActiveRecords(): List<LocalRecord>

    @Query("SELECT * FROM local_records WHERE externalId=:externalId LIMIT 1")
    suspend fun getRecord(externalId: Int): LocalRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecord(record: LocalRecord)

}