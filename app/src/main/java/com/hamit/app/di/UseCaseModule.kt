package com.hamit.app.di

import com.hamit.domain.useCases.addon.ReceiveAddonListUseCase
import com.hamit.domain.useCases.addon.ReceiveAddonUseCase
import com.hamit.domain.useCases.config.ReceiveConfigUseCase
import com.hamit.domain.useCases.like.AddLikeUseCase
import com.hamit.domain.useCases.like.ReceiveFavoriteAddonsUseCase
import com.hamit.domain.useCases.like.ReceiveLikeTotalSizeUseCase
import com.hamit.domain.useCases.problem.SendProblemUseCase
import com.hamit.domain.useCases.region.ReceiveRegionUseCase
import com.hamit.domain.useCases.suggest.SubmitSuggestUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { ReceiveAddonListUseCase(get()) }
    single { ReceiveAddonUseCase(get()) }
    single { ReceiveConfigUseCase(get()) }
    single { AddLikeUseCase(get()) }
    single { ReceiveFavoriteAddonsUseCase(get()) }
    single { ReceiveLikeTotalSizeUseCase(get()) }
    single { SendProblemUseCase(get()) }
    single { ReceiveRegionUseCase(get()) }
    single { SubmitSuggestUseCase(get()) }
}