package com.hamit.data.source.remote

import com.hamit.data.BuildConfig.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiFactory {

    val client = HttpClient(Android) {
        expectSuccess = true

        engine {
            connectTimeout = 60_000
            socketTimeout = 60_000
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
        }

        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }

        defaultRequest {
            contentType(ContentType.Application.Json.withParameter("charset", "utf-8"))
            url(BASE_URL)
        }

        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.NONE
        }
    }

}
