package com.l13devstudio.data.repository

import com.l13devstudio.data.exceptionHandle
import com.l13devstudio.data.source.remote.api.AddonApi
import com.l13devstudio.data.source.remote.dto.addon.AddonDto
import com.l13devstudio.data.source.remote.dto.addon.AddonListResponse
import com.l13devstudio.data.transforms.AddonTransformer
import com.l13devstudio.domain.entity.addon.AddonSortType
import com.l13devstudio.domain.entity.addon.AddonType
import com.l13devstudio.domain.entity.appConfig.AppConfigProvider
import com.l13devstudio.domain.repository.AddonRepository
import io.ktor.client.call.body

class AddonRepositoryImpl(
    private val addonApi: AddonApi,
    private val transformer: AddonTransformer,
    private val configProvider: AppConfigProvider
) : AddonRepository {

    override suspend fun receiveAddonList(
        q: String,
        offset: Int,
        type: AddonType?,
        sortType: AddonSortType,
        limit: Int,
    ) = try {
        val response = addonApi.fetchAddonList(
            q = q,
            skip = offset,
            category = type,
            sortKey = sortType.toString(),
            take = limit,
            appId = configProvider.getConfig().appId
        ).body<AddonListResponse>()
        Result.success(transformer.toEntity(response.items))
    } catch (e: Exception){
        e.printStackTrace()
        val exception = e as? java.util.concurrent.CancellationException ?: e.exceptionHandle()
        Result.failure(exception)
    }


    override suspend fun receiveAddon(id: Int) = try {
        val addon = addonApi.fetchAddon(id).body<AddonDto>()
        Result.success(transformer.toEntity(addon))
    } catch (e: Exception){
        e.printStackTrace()
        val exception = e as? java.util.concurrent.CancellationException ?: e.exceptionHandle()
        Result.failure(e.exceptionHandle())
    }


}