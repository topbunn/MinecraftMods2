package com.hamit.data.source.remote.api

import com.hamit.data.source.remote.ApiFactory
import com.hamit.data.source.remote.dto.suggest.SuggestDto
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class SuggestApi(
    private val api: ApiFactory
) {

    suspend fun submitSuggest(id: Int, suggest: SuggestDto) = api.client.post("v1/apps/$id/mod/recommend"){
        setBody(suggest)
    }

}