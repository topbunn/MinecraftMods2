package com.l13devstudio.app.di

import com.l13devstudio.domain.useCases.addon.ReceiveAddonListUseCase
import com.l13devstudio.domain.useCases.addon.ReceiveAddonUseCase
import com.l13devstudio.domain.useCases.config.ReceiveConfigUseCase
import com.l13devstudio.domain.useCases.download.DownloadFileUseCase
import com.l13devstudio.domain.useCases.file.CreateFileUseCase
import com.l13devstudio.domain.useCases.file.DeleteFileUseCase
import com.l13devstudio.domain.useCases.file.FileExistsUseCase
import com.l13devstudio.domain.useCases.file.OpenFileUseCase
import com.l13devstudio.domain.useCases.like.AddLikeUseCase
import com.l13devstudio.domain.useCases.like.ReceiveLikeAddonsUseCase
import com.l13devstudio.domain.useCases.like.ReceiveLikeTotalSizeUseCase
import com.l13devstudio.domain.useCases.problem.SendProblemUseCase
import com.l13devstudio.domain.useCases.region.ReceiveRegionUseCase
import com.l13devstudio.domain.useCases.suggest.SubmitSuggestUseCase
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