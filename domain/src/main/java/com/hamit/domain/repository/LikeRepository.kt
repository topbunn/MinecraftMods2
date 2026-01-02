package com.hamit.domain.repository

import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.domain.entity.like.LikeEntity

interface LikeRepository {

    suspend fun receiveLikeTotalSize(): Int
    suspend fun receiveFavoriteAddons(offset: Int, limit: Int): Result<List<AddonEntity>>
    suspend fun addLike(like: LikeEntity)

}