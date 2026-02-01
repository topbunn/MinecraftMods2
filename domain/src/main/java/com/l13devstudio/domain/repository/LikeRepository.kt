package com.l13devstudio.domain.repository

import com.l13devstudio.domain.entity.addon.AddonEntity
import com.l13devstudio.domain.entity.like.LikeEntity

interface LikeRepository {

    suspend fun receiveLikeTotalSize(): Int
    suspend fun receiveFavoriteAddons(offset: Int, limit: Int): Result<List<AddonEntity>>
    suspend fun addLike(like: LikeEntity)

}