package com.l13devstudio.data.repository

import com.l13devstudio.data.exceptionHandle
import com.l13devstudio.data.source.local.database.dao.LikeDao
import com.l13devstudio.data.source.local.database.entity.LikeDbo
import com.l13devstudio.data.source.remote.api.AddonApi
import com.l13devstudio.data.source.remote.dto.addon.AddonDto
import com.l13devstudio.data.transforms.AddonTransformer
import com.l13devstudio.domain.entity.like.LikeEntity
import com.l13devstudio.domain.repository.LikeRepository
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class LikeRepositoryImpl(
    private val likeDao: LikeDao,
    private val addonApi: AddonApi,
    private val addonTransform: AddonTransformer,
) : LikeRepository {

    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable -> throw throwable }

    override suspend fun receiveLikeTotalSize() = likeDao.getActiveLikes().size

    override suspend fun receiveFavoriteAddons(offset: Int, limit: Int) = coroutineScope{
        try {
            val favoriteIds = likeDao.getActiveLikes()
                .drop(offset)
                .take(limit)
                .map { it.addonId }
            val result = favoriteIds.map {
                async(SupervisorJob() + coroutineExceptionHandler){
                    val mod = addonApi.fetchAddon(it).body<AddonDto>()
                    addonTransform.toEntity(mod)
                }
            }.awaitAll()
            Result.success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e.exceptionHandle())
        }

    }

    override suspend fun addLike(like: LikeEntity) {
        val oldLike = likeDao.getLike(like.addonId)
        val newLike = like.copy(id = oldLike?.id ?: 0)
        likeDao.saveLike(LikeDbo.fromEntity(newLike))
    }


}