package com.hamit.data.source.remote.api

import com.hamit.data.source.remote.ApiFactory
import io.ktor.client.request.get

class RegionApi(private val api: ApiFactory){

    suspend fun fetchRegionData() = api.client.get("https://ipapi.co/json")

}