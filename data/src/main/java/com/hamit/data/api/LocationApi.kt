package com.hamit.data.api

import retrofit2.http.GET
import com.hamit.data.api.dto.location.LocationResponse

interface LocationApi {

    @GET("https://ipapi.co/json")
    suspend fun getInfo(): LocationResponse

}