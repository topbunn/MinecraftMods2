package com.l13devstudio.data.repository

import com.l13devstudio.data.source.remote.api.ProblemApi
import com.l13devstudio.data.source.remote.dto.problem.ProblemDto
import com.l13devstudio.domain.entity.appConfig.AppConfigProvider
import com.l13devstudio.domain.entity.problem.ProblemEntity
import com.l13devstudio.domain.repository.ProblemRepository
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