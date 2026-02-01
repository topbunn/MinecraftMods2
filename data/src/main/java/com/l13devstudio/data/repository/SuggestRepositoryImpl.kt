package com.l13devstudio.data.repository

import com.l13devstudio.data.source.remote.api.SuggestApi
import com.l13devstudio.data.source.remote.dto.suggest.SuggestDto
import com.l13devstudio.domain.entity.appConfig.AppConfigProvider
import com.l13devstudio.domain.entity.suggest.SuggestEntity
import com.l13devstudio.domain.repository.SuggestRepository

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