package com.hamit.data.api

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url
import com.hamit.android.utils.receiveLanguageFromDevice
import com.hamit.data.api.dto.mods.AppInfoDto
import com.hamit.domain.entity.ProblemEntity
import com.hamit.data.api.dto.mods.GetModsResponse
import com.hamit.data.api.dto.mods.ModDto
import com.hamit.domain.entity.addon.AddonType

interface ModsApi {

    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody

    @POST("/v1/apps/{id}/issue")
    suspend fun createIssue(@Path("id") id: Int, @Body issue: ProblemEntity)

    @GET("/v1/apps/{appId}/mod/{status}")
    suspend fun getMods(
        @Path("appId") appId: Int,
        @Path("status") status: String = "actived",
        @Header("Language") language: String = receiveLanguageFromDevice(),
        @Query("q") q: String,
        @Query("category") category: AddonType?,
        @Query("sort_key") sortKey: String,
        @Query("skip") skip: Int,
        @Query("take") take: Int,
        @Query("sort_value") sortValue: String = "asc",
    ): GetModsResponse

    @GET("/v1/mod/{id}")
    suspend fun getMod(
        @Path("id") id: Int,
        @Header("Language") language: String = receiveLanguageFromDevice()
    ): ModDto

    @GET("/v1/apps")
    suspend fun getApps(
        @Header("Language") language: String = receiveLanguageFromDevice()
    ): List<AppInfoDto>

    @GET("/v1/apps/{id}")
    suspend fun loadConfig(
        @Path("id") id: Int,
    ): AppInfoDto

}