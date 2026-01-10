package com.hamit.data.repository

import com.hamit.android.Error
import com.hamit.android.Maintenance
import com.hamit.android.NoInternet
import com.hamit.data.source.local.database.dao.LikeDao
import com.hamit.data.source.local.database.entity.LikeDbo
import com.hamit.data.source.remote.api.AddonApi
import com.hamit.data.source.remote.dto.addon.AddonDto
import com.hamit.data.transforms.AddonTransformer
import com.hamit.domain.entity.like.LikeEntity
import com.hamit.domain.repository.LikeRepository
import io.ktor.client.call.body
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class LikeRepositoryImpl(
    private val likeDao: LikeDao,
    private val addonApi: AddonApi,
    private val addonTransform: AddonTransformer,
) : LikeRepository {

    override suspend fun receiveLikeTotalSize() = likeDao.getActiveLikes().size

    override suspend fun receiveFavoriteAddons(offset: Int, limit: Int) = try {
        val favoriteIds = likeDao.getActiveLikes()
            .drop(offset)
            .take(limit)
            .map { it.addonId }
        val result = favoriteIds.map {
            coroutineScope {
                async {
                    try {
                        val mod = addonApi.fetchAddon(it).body<AddonDto>()
                        addonTransform.toEntity(mod)
                    } catch (_: Exception) { null }
                }
            }
        }.awaitAll().filterNotNull()
        Result.success(result)
    } catch (e: Exception){
        e.printStackTrace()
        val exception = when (e) {

            is java.net.UnknownHostException,
            is UnresolvedAddressException -> NoInternet

            is javax.net.ssl.SSLHandshakeException,
            is javax.net.ssl.SSLException -> Maintenance

            is io.ktor.client.network.sockets.ConnectTimeoutException,
            is io.ktor.client.plugins.HttpRequestTimeoutException,
            is java.net.SocketTimeoutException,
            is java.net.ConnectException -> Maintenance

            is io.ktor.client.plugins.ServerResponseException -> Maintenance

            else -> Error
        }
        Result.failure(exception)
    }

    override suspend fun addLike(like: LikeEntity) {
        val oldLike = likeDao.getLike(like.addonId)
        val newLike = like.copy(id = oldLike?.id ?: 0)
        likeDao.saveLike(LikeDbo.fromEntity(newLike))
    }


}