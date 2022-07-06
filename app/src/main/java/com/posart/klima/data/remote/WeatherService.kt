package com.posart.klima.data.remote

import com.posart.klima.data.remote.entities.WeatherForecastNetwork

interface WeatherService {

    suspend fun getWeatherForecast(lat: Double, lon: Double, excludedParts: String, unitSystem: String): WeatherForecastNetwork?

}