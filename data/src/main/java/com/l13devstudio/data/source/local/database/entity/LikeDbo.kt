package com.l13devstudio.data.source.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.l13devstudio.domain.entity.like.LikeEntity

@Entity("likes")
data class LikeDbo (
    @PrimaryKey(true)
    val id: Int = 0,
    val addonId: Int,
    val isActive: Boolean
){
    companion object{
        fun fromEntity(entity: LikeEntity) = LikeDbo(entity.id, entity.addonId, entity.isActive)
    }
}