package com.hamit.app.di

import com.hamit.domain.useCases.addon.ReceiveAddonListUseCase
import com.hamit.domain.useCases.addon.ReceiveAddonUseCase
import com.hamit.domain.useCases.config.ReceiveConfigUseCase
import com.hamit.domain.useCases.download.DownloadFileUseCase
import com.hamit.domain.useCases.file.CreateFileUseCase
import com.hamit.domain.useCases.file.DeleteFileUseCase
import com.hamit.domain.useCases.file.FileExistsUseCase
import com.hamit.domain.useCases.file.OpenFileUseCase
import com.hamit.domain.useCases.like.AddLikeUseCase
import com.hamit.domain.useCases.like.ReceiveLikeAddonsUseCase
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
    single { ReceiveLikeAddonsUseCase(get()) }
    single { ReceiveLikeTotalSizeUseCase(get()) }
    single { SendProblemUseCase(get()) }
    single { ReceiveRegionUseCase(get()) }
    single { SubmitSuggestUseCase(get()) }
    single { DownloadFileUseCase(get()) }
    single { CreateFileUseCase(get()) }
    single { FileExistsUseCase(get()) }
    single { OpenFileUseCase(get()) }
    single { DeleteFileUseCase(get()) }
}