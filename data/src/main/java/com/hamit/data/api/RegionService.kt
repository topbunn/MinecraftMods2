package com.hamit.data.api

import retrofit2.http.GET
import com.hamit.data.api.dto.location.RegionMetadata

interface RegionService {

    @GET("https://ipapi.co/json")
    suspend fun fetchRegionData(): RegionMetadata

}
