package org.example.project.features.common.data.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val BASE_URL = "exercisedb.p.rapidapi.com"
const val API_VERSION = "v1"

val httpClient = HttpClient {
    // Base request setup
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = BASE_URL
            path("api", API_VERSION)
            header("X-RapidAPI-Key", "75bb857f61msh9ac96c92e5a3a2ap10cea5jsn3510e9295bd8")
            header("X-RapidAPI-Host", BASE_URL)
        }
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
        })
    }

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
        sanitizeHeader { header ->
            header == HttpHeaders.Authorization || header == "X-RapidAPI-Key"
        }
    }
}