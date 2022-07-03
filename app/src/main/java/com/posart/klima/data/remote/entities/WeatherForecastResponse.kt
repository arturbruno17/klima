package com.posart.klima.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherForecast(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerialName("timezone_offset") val timezoneOffset: Int,
    @SerialName("current") val currentForecast: CurrentForecast,
    @SerialName("hourly") val hourlyForecast: List<HourlyForecast>,
    @SerialName("daily") val dailyForecast: List<DailyForecast>
)


@Serializable
data class CurrentForecast(
    @SerialName("dt") val dateTime: Long,
    val sunrise: Long,
    val sunset: Long,
    @SerialName("temp") val temperature: Float,
    @SerialName("feels_like") val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val uvi: Double,
    val clouds: Double,
    @SerialName("wind_speed") val windSpeed: Double,
    @SerialName("wind_deg") val windDeg: Int,
    @SerialName("weather") val weatherImage: List<WeatherImage>,

    )

@Serializable
data class HourlyForecast(
    @SerialName("dt") val dateTime: Long,
    @SerialName("temp") val temperature: Float
)

@Serializable
data class DailyForecast(
    @SerialName("dt") val dateTime: Long,
    @SerialName("temp") val temperature: Temperatures,
    @SerialName("weather") val weatherImage: List<WeatherImage>,
)


@Serializable
data class Temperatures(
    @SerialName("min") val minTemperature: Double,
    @SerialName("max") val maxTemperature: Double,

)

@Serializable
data class WeatherImage(
    val id: Int
)