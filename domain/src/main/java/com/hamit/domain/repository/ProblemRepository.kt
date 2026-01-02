package com.hamit.domain.repository

import com.hamit.domain.entity.problem.ProblemEntity

interface ProblemRepository {

    suspend fun sendProblem(problem: ProblemEntity): Result<Unit>

}