package com.hamit.data.repository

import com.hamit.data.source.local.database.dao.LikeDao
import com.hamit.data.source.local.database.entity.LikeDbo
import com.hamit.data.source.remote.api.AddonApi
import com.hamit.data.source.remote.dto.addon.AddonDto
import com.hamit.data.transforms.AddonTransformer
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.domain.entity.like.LikeEntity
import com.hamit.domain.repository.LikeRepository
import io.ktor.client.call.body

class LikeRepositoryImpl(
    private val likeDao: LikeDao,
    private val addonApi: AddonApi,
    private val addonTransform: AddonTransformer,
) : LikeRepository {

    override suspend fun receiveLikeTotalSize() = likeDao.getActiveLikes().size

    override suspend fun receiveFavoriteAddons(offset: Int, limit: Int) = runCatching {
        val favoriteIds = likeDao.getActiveLikes()
            .drop(offset)
            .take(limit)
            .map { it.addonId }
        val mods = mutableListOf<AddonEntity>()
        favoriteIds.forEach {
            try {
                val mod = addonApi.fetchAddon(it).body<AddonDto>()
                mods.add(addonTransform.toEntity(mod))
            } catch (_: Exception) {
            }
        }
        return@runCatching mods
    }

    override suspend fun addLike(like: LikeEntity) {
        val oldLike = likeDao.getLike(like.addonId)
        val newLike = like.copy(id = oldLike?.id ?: 0)
        likeDao.saveLike(LikeDbo.fromEntity(newLike))
    }


}