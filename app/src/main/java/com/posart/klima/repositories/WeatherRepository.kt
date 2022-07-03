package com.posart.klima.repositories

import com.posart.klima.data.remote.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val weatherService: WeatherService = WeatherService.create()
) {

    suspend fun getWeatherForecast(lat: Double, lon: Double, excludedParts: String) =
        withContext(Dispatchers.IO) {
            weatherService.getWeatherForecast(lat, lon, excludedParts)
        }

}