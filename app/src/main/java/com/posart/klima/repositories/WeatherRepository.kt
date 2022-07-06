package com.posart.klima.repositories

import com.posart.klima.data.remote.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherService: WeatherService
) {

    suspend fun getWeatherForecast(lat: Double, lon: Double, excludedParts: String, unitSystem: String) =
        withContext(Dispatchers.IO) {
            weatherService.getWeatherForecast(lat, lon, excludedParts, unitSystem)
        }

}