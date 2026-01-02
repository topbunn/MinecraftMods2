package com.hamit.data.repository

import com.hamit.data.source.remote.api.AddonApi
import com.hamit.data.source.remote.dto.addon.AddonDto
import com.hamit.data.source.remote.dto.addon.AddonListResponse
import com.hamit.data.transforms.AddonTransformer
import com.hamit.domain.entity.addon.AddonSortType
import com.hamit.domain.entity.addon.AddonType
import com.hamit.domain.entity.appConfig.AppConfigProvider
import com.hamit.domain.repository.AddonRepository
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
    ) = runCatching {
        val response = addonApi.fetchAddonList(
            q = q,
            skip = offset,
            category = type,
            sortKey = sortType.toString(),
            take = limit,
            appId = configProvider.getConfig().appId
        ).body<AddonListResponse>()
        transformer.toEntity(response.items)
    }


    override suspend fun receiveAddon(id: Int) = runCatching {
        val addon = addonApi.fetchAddon(id).body<AddonDto>()
        transformer.toEntity(addon)
    }


}