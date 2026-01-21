package com.hamit.data.repository

import com.hamit.data.source.remote.api.SuggestApi
import com.hamit.data.source.remote.dto.suggest.SuggestDto
import com.hamit.domain.entity.appConfig.AppConfigProvider
import com.hamit.domain.entity.suggest.SuggestEntity
import com.hamit.domain.repository.SuggestRepository

class SuggestRepositoryImpl(
    private val api: SuggestApi,
    private val configProvider: AppConfigProvider
): SuggestRepository {

    override suspend fun submitSuggest(suggest: SuggestEntity) = runCatching {
        api.submitSuggest(
            id = configProvider.getConfig().appId,
            suggest = SuggestDto.fromEntity(suggest)
        )
        return@runCatching
    }

}