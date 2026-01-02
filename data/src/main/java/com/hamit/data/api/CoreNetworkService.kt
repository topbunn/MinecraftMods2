package com.hamit.data.api

import com.hamit.android.utills.receiveLanguageFromDevice
import com.hamit.data.api.dto.mods.EntryDto
import com.hamit.data.api.dto.mods.EntryListResponse
import com.hamit.data.api.dto.mods.SystemConfigDto
import com.hamit.domain.entity.ProblemEntity
import com.hamit.domain.entity.addon.AddonType
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface CoreNetworkService {

    @Streaming
    @GET
    suspend fun streamResource(@Url url: String): ResponseBody

    @POST("/v1/apps/{id}/issue")
    suspend fun reportIssue(@Path("id") id: Int, @Body issue: ProblemEntity)

    @GET("/v1/apps/{appId}/mod/{status}")
    suspend fun fetchDataList(
        @Path("appId") appId: Int,
        @Path("status") status: String = "actived",
        @Header("Language") language: String = receiveLanguageFromDevice(),
        @Query("q") q: String,
        @Query("category") category: AddonType?,
        @Query("sort_key") sortKey: String,
        @Query("skip") skip: Int,
        @Query("take") take: Int,
        @Query("sort_value") sortValue: String = "asc",
    ): EntryListResponse

    @GET("/v1/mod/{id}")
    suspend fun fetchDetails(
        @Path("id") id: Int,
        @Header("Language") language: String = receiveLanguageFromDevice()
    ): EntryDto

    @GET("/v1/apps")
    suspend fun fetchApplicationList(
        @Header("Language") language: String = receiveLanguageFromDevice()
    ): List<SystemConfigDto>

    @GET("/v1/apps/{id}")
    suspend fun loadConfiguration(
        @Path("id") id: Int,
    ): SystemConfigDto

}
