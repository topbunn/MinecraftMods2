package com.hamit.data.source.remote.api

import com.hamit.android.utills.receiveLanguageFromDevice
import com.hamit.data.source.remote.ApiFactory
import com.hamit.domain.entity.addon.AddonType
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter


class AddonApi(private val api: ApiFactory) {

    suspend fun fetchAddonList(
        appId: Int,
        status: String = "actived",
        language: String = receiveLanguageFromDevice(),
        q: String,
        category: AddonType?,
        sortKey: String,
        skip: Int,
        take: Int,
        sortValue: String = "asc",
    ) = api.client.get("v1/apps/$appId/mod/$status") {
        header("Language", language)
        parameter("q", q)
        parameter("category", category)
        parameter("sort_key", sortKey)
        parameter("skip", skip)
        parameter("take", take)
        parameter("sort_value", sortValue)
    }

    suspend fun fetchAddon(
        id: Int,
        language: String = receiveLanguageFromDevice()
    ) = api.client.get("v1/mod/$id") {
        header("Language", language)
    }
}