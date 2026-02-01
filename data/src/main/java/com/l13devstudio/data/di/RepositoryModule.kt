package com.l13devstudio.data.di

import com.l13devstudio.data.repository.AddonRepositoryImpl
import com.l13devstudio.data.repository.ConfigRepositoryImpl
import com.l13devstudio.data.repository.DownloadRepositoryImpl
import com.l13devstudio.data.repository.FileRepositoryImpl
import com.l13devstudio.data.repository.LikeRepositoryImpl
import com.l13devstudio.data.repository.ProblemRepositoryImpl
import com.l13devstudio.data.repository.RegionRepositoryImpl
import com.l13devstudio.data.repository.SuggestRepositoryImpl
import com.l13devstudio.domain.repository.AddonRepository
import com.l13devstudio.domain.repository.ConfigRepository
import com.l13devstudio.domain.repository.DownloadRepository
import com.l13devstudio.domain.repository.FileRepository
import com.l13devstudio.domain.repository.LikeRepository
import com.l13devstudio.domain.repository.ProblemRepository
import com.l13devstudio.domain.repository.RegionRepository
import com.l13devstudio.domain.repository.SuggestRepository
import org.koin.dsl.module

internal val repositoryModule = module {
    single<AddonRepository> {
        AddonRepositoryImpl(get(), get(), get())
    }
    single<RegionRepository> {
        RegionRepositoryImpl(get(), get())
    }
    single<ConfigRepository> {
        ConfigRepositoryImpl(get(), get(), get())
    }
    single<LikeRepository> {
        LikeRepositoryImpl(get(),get(),get())
    }
    single<ProblemRepository> {
        ProblemRepositoryImpl(get(), get())
    }
    single<SuggestRepository> {
        SuggestRepositoryImpl(get(), get())
    }
    single<FileRepository> {
        FileRepositoryImpl(get())
    }
    single<DownloadRepository> {
        DownloadRepositoryImpl(get(), get())
    }

}