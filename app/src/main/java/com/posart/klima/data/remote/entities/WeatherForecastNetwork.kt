package com.posart.klima.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherForecastNetwork(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerialName("timezone_offset") val timezoneOffset: Int,
    @SerialName("current") val currentForecast: CurrentForecastNetwork,
    @SerialName("hourly") val hourlyForecast: List<HourlyForecastNetwork>,
    @SerialName("daily") val dailyForecast: List<DailyForecastNetwork>
)


@Serializable
data class CurrentForecastNetwork(
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
    @SerialName("weather") val weatherImage: List<WeatherImageNetwork>,

    )

@Serializable
data class HourlyForecastNetwork(
    @SerialName("dt") val dateTime: Long,
    @SerialName("temp") val temperature: Float
)

@Serializable
data class DailyForecastNetwork(
    @SerialName("dt") val dateTime: Long,
    @SerialName("temp") val temperature: TemperaturesNetwork,
    @SerialName("weather") val weatherImage: List<WeatherImageNetwork>,
)


@Serializable
data class TemperaturesNetwork(
    @SerialName("min") val minTemperature: Double,
    @SerialName("max") val maxTemperature: Double,

)

@Serializable
data class WeatherImageNetwork(
    val id: Int,
    val icon: String
)