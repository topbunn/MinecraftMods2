package com.hamit.domain.repository

import com.hamit.domain.entity.suggest.SuggestEntity

interface SuggestRepository {

    suspend fun submitSuggest(suggest: SuggestEntity): Result<Unit>

}