package com.l13devstudio.domain.repository

import com.l13devstudio.domain.entity.problem.ProblemEntity

interface ProblemRepository {

    suspend fun sendProblem(problem: ProblemEntity): Result<Unit>

}