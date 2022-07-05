package com.posart.klima.data.remote

import android.util.Log
import com.posart.klima.BuildConfig
import com.posart.klima.data.remote.entities.WeatherForecast
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import java.lang.Exception

class WeatherServiceImpl(
    private val client: HttpClient
) : WeatherService {

    override suspend fun getWeatherForecast(
        lat: Double,
        lon: Double,
        excludedParts: String,
        unitSystem: String
    ): WeatherForecast? {
        return try {
            client.get(HttpRoutes.WEATHER_FORECAST_URL) {
                url {
                    parameters.append("lat", lat.toString())
                    parameters.append("lon", lon.toString())
                    parameters.append("exclude", excludedParts)
                    parameters.append("appid", BuildConfig.API_KEY)
                    parameters.append("units", unitSystem)
                }
            }.body()
        } catch (e: RedirectResponseException) {
            Log.e("WEATHER_REQUESTS", "Erro de redirecionamento")
            null
        } catch (e: ClientRequestException) {
            Log.e("WEATHER_REQUESTS", "Erro no cliente")
            null
        } catch (e: ServerResponseException) {
            Log.e("WEATHER_REQUESTS", "Erro no servidor")
            null
        } catch (e: Exception) {
            Log.e("WEATHER_REQUESTS", e.stackTraceToString())
            null
        }
    }

}