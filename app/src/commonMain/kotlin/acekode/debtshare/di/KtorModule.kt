package acekode.debtshare.di

import acekode.debtshare.KtorConfigPlugin
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val ktorModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 10_000
            }
            if (KtorConfigPlugin.IS_DEBUG) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) = println(message)
                    }
                    level = LogLevel.BODY
                }
            }
        }
    }
}
