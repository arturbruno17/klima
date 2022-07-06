package com.posart.klima.data.remote

import com.posart.klima.data.remote.entities.WeatherForecastNetwork
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

interface WeatherService {

    suspend fun getWeatherForecast(lat: Double, lon: Double, excludedParts: String, unitSystem: String): WeatherForecastNetwork?

    companion object {
        fun create(): WeatherService {
            return WeatherServiceImpl(
                client = HttpClient(Android) {
                    install(ContentNegotiation) {
                        json(Json {
                            ignoreUnknownKeys = true
                        })
                    }
                }
            )
        }
    }

}