package com.hamit.domain.useCases.suggest

import com.hamit.domain.entity.suggest.SuggestEntity
import com.hamit.domain.repository.SuggestRepository

class SubmitSuggestUseCase(
    private val repository: SuggestRepository
) {

    suspend operator fun invoke(suggest: SuggestEntity) = repository.submitSuggest(suggest)

}