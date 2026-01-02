package com.hamit.data.source.remote.api

import com.hamit.data.source.remote.ApiFactory
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel

class FileApi(private val api: ApiFactory) {

    suspend fun stream(url: String) = api.client.get(url).bodyAsChannel()

}