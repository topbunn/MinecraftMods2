package com.l13devstudio.domain.useCases.suggest

import com.l13devstudio.domain.entity.suggest.SuggestEntity
import com.l13devstudio.domain.repository.SuggestRepository

class SubmitSuggestUseCase(
    private val repository: SuggestRepository
) {

    suspend operator fun invoke(suggest: SuggestEntity) = repository.submitSuggest(suggest)

}