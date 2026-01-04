package com.hamit.data.source.remote.api

import com.hamit.data.source.remote.ApiFactory
import com.hamit.data.source.remote.dto.suggest.SuggestDto

class SuggestApi(
    private val api: ApiFactory
) {

    suspend fun submitSuggest(suggest: SuggestDto){}

}