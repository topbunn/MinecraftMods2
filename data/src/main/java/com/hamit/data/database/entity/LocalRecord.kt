package com.hamit.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("local_records")
data class LocalRecord (
    @PrimaryKey(true)
    val id: Int = 0,
    val externalId: Int,
    val isActive: Boolean
)