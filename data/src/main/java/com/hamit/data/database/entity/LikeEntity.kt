package com.hamit.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("local_records")
data class LikeEntity (
    @PrimaryKey(true)
    val id: Int = 0,
    val addonId: Int,
    val isActive: Boolean
)