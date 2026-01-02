package com.hamit.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hamit.data.source.local.database.entity.LikeDbo

@Dao
interface LikeDao {

    @Query("SELECT * FROM likes WHERE isActive = 1")
    suspend fun getActiveLikes(): List<LikeDbo>

    @Query("SELECT * FROM likes WHERE addonId=:id LIMIT 1")
    suspend fun getLike(id: Int): LikeDbo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLike(like: LikeDbo)

}