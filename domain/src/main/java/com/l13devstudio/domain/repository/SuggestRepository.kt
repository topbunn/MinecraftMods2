package com.l13devstudio.domain.repository

import com.l13devstudio.domain.entity.suggest.SuggestEntity

interface SuggestRepository {

    suspend fun submitSuggest(suggest: SuggestEntity): Result<Unit>

}