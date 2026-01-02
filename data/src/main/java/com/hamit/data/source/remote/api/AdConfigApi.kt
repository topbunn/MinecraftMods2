package com.hamit.data.source.remote.api

import com.hamit.data.source.remote.ApiFactory
import io.ktor.client.request.get

class AdConfigApi(private val api: ApiFactory) {

    suspend fun loadConfiguration(id: Int) = api.client.get("v1/apps/$id")
}