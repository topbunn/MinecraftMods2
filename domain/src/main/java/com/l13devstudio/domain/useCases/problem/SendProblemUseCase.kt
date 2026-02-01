package com.l13devstudio.domain.useCases.problem

import com.l13devstudio.domain.entity.problem.ProblemEntity
import com.l13devstudio.domain.repository.ProblemRepository

class SendProblemUseCase(
    private val repository: ProblemRepository
) {

    suspend operator fun invoke(problem: ProblemEntity) = repository.sendProblem(problem)


}