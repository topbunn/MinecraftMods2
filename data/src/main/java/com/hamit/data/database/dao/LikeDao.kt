package com.hamit.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hamit.data.database.entity.LikeEntity

@Dao
interface LikeDao {

    @Query("SELECT * FROM local_records WHERE isActive = 1")
    suspend fun getActiveRecords(): List<LikeEntity>

    @Query("SELECT * FROM local_records WHERE addonId=:externalId LIMIT 1")
    suspend fun getRecord(externalId: Int): LikeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecord(record: LikeEntity)

}