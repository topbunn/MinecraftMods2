package com.hamit.data.source.remote.api

import com.hamit.data.source.remote.ApiFactory
import com.hamit.data.source.remote.dto.problem.ProblemDto
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ProblemApi(private val api: ApiFactory) {

    suspend fun report(id: Int, problem: ProblemDto) = api.client.post("v1/apps/$id/issue") {
        setBody(problem)
    }

}