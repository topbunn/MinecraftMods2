package com.hamit.data.di

import com.hamit.data.repository.AddonRepositoryImpl
import com.hamit.data.repository.ConfigRepositoryImpl
import com.hamit.data.repository.LikeRepositoryImpl
import com.hamit.data.repository.ProblemRepositoryImpl
import com.hamit.data.repository.RegionRepositoryImpl
import com.hamit.domain.repository.AddonRepository
import com.hamit.domain.repository.ConfigRepository
import com.hamit.domain.repository.LikeRepository
import com.hamit.domain.repository.ProblemRepository
import com.hamit.domain.repository.RegionRepository
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

}