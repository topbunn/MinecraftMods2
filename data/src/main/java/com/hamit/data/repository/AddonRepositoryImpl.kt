package com.hamit.data.repository

import com.hamit.android.Error
import com.hamit.android.Maintenance
import com.hamit.android.NoInternet
import com.hamit.data.source.remote.api.AddonApi
import com.hamit.data.source.remote.dto.addon.AddonDto
import com.hamit.data.source.remote.dto.addon.AddonListResponse
import com.hamit.data.transforms.AddonTransformer
import com.hamit.domain.entity.addon.AddonSortType
import com.hamit.domain.entity.addon.AddonType
import com.hamit.domain.entity.appConfig.AppConfigProvider
import com.hamit.domain.repository.AddonRepository
import io.ktor.client.call.body
import io.ktor.util.network.UnresolvedAddressException

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


    override suspend fun receiveAddon(id: Int) = runCatching {
        val addon = addonApi.fetchAddon(id).body<AddonDto>()
        transformer.toEntity(addon)
    }


}