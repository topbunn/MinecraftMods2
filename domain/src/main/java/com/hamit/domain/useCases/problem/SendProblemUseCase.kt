package com.hamit.domain.useCases.problem

import com.hamit.domain.entity.problem.ProblemEntity
import com.hamit.domain.repository.ProblemRepository

class SendProblemUseCase(
    private val repository: ProblemRepository
) {

    suspend operator fun invoke(problem: ProblemEntity) = repository.sendProblem(problem)


}