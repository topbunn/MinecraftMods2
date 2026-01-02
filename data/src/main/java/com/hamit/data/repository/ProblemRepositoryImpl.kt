package com.hamit.data.repository

import com.hamit.data.source.remote.api.ProblemApi
import com.hamit.data.source.remote.dto.problem.ProblemDto
import com.hamit.domain.entity.appConfig.AppConfigProvider
import com.hamit.domain.entity.problem.ProblemEntity
import com.hamit.domain.repository.ProblemRepository
import io.ktor.client.call.body

class ProblemRepositoryImpl(
    private val problemApi: ProblemApi,
    private val configProvider: AppConfigProvider
): ProblemRepository{

    override suspend fun sendProblem(problem: ProblemEntity): Result<Unit> = runCatching {
        problemApi.report(
            problem = ProblemDto.fromEntity(problem),
            id = configProvider.getConfig().appId
        )
    }

}