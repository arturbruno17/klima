package com.posart.klima.ui.entities

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.posart.klima.R
import com.posart.klima.UnitSystem
import com.posart.klima.data.remote.entities.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt

data class WeatherForecast(
    val temperatureUnit: String,
    val speedUnit: String,
    val currentForecast: CurrentForecast,
    val hourlyForecast: List<HourlyForecast>,
    val dailyForecast: List<DailyForecast>
)


data class CurrentForecast(
    val sunrise: String,
    val sunset: String,
    val temperature: Int,
    val feelsLike: Int,
    val pressure: String,
    val humidity: String,
    val uvi: Double,
    val clouds: String,
    val windSpeed: Int,
    val windDeg: String,
    val weatherImage: List<WeatherImage>
)

data class HourlyForecast(
    val dateTime: String,
    val temperature: Int
)

data class DailyForecast(
    val dateTime: String,
    val temperature: Temperatures,
    val weatherImage: List<WeatherImage>
)


data class Temperatures(
    val minTemperature: Int,
    val maxTemperature: Int,
)

data class WeatherImage(
    val id: Int,
    @DrawableRes val icon: Int,
    @StringRes val title: Int
)

fun WeatherForecastNetwork.asModel(unitSystem: String): WeatherForecast {
    return WeatherForecast(
        temperatureUnit = if (unitSystem == UnitSystem.METRIC.value) "ºC" else "ºF",
        speedUnit = if (unitSystem == UnitSystem.METRIC.value) "m/s" else "mph",
        currentForecast = currentForecast.asModel(timezoneOffset),
        hourlyForecast = hourlyForecast.map {
            it.asModel(timezoneOffset)
        },
        dailyForecast = dailyForecast.map {
            it.asModel(timezoneOffset)
        }
    )
}

fun CurrentForecastNetwork.asModel(timezoneOffset: Int = 0): CurrentForecast {
    val hectopascalToATM = 1013

    return CurrentForecast(
        sunrise = fromUnixDateTimeToFormattedTime(sunrise + timezoneOffset),
        sunset = fromUnixDateTimeToFormattedTime(sunset + timezoneOffset),
        temperature = temperature.roundToInt(),
        feelsLike = feelsLike.roundToInt(),
        pressure = "${pressure / hectopascalToATM}atm",
        humidity = "${humidity}%",
        uvi = uvi,
        clouds = "${clouds.roundToInt()}%",
        windSpeed = windSpeed.roundToInt(),
        windDeg = "${windDeg}º",
        weatherImage = weatherImage.map {
            it.asModel()
        }
    )
}

fun WeatherImageNetwork.asModel(): WeatherImage {
    val titleMap = mapOf(
        2 to R.string.thunderstorm, 3 to R.string.drizzle, 5 to R.string.rain,
        6 to R.string.snow, 701 to R.string.mist, 711 to R.string.smoke, 721 to R.string.haze,
        731 to R.string.dust, 741 to R.string.fog, 751 to R.string.sand,
        761 to R.string.dust, 762 to R.string.ash, 771 to R.string.squall,
        781 to R.string.tornado, 800 to R.string.clear, 8 to R.string.clouds
    )

    val dayIconsMap = mapOf(
        2 to R.drawable.thunderstorm, 3 to R.drawable.shower_rain, 5 to R.drawable.day_rain,
        6 to R.drawable.snow, 7 to R.drawable.sun_clear_sky, 8 to R.drawable.clouds,
        800 to R.drawable.sun_clear_sky
    )

    val nightIconsMap = mapOf(
        2 to R.drawable.thunderstorm, 3 to R.drawable.shower_rain, 5 to R.drawable.night_rain,
        6 to R.drawable.snow, 7 to R.drawable.night_clear_sky, 8 to R.drawable.clouds,
        800 to R.drawable.night_clear_sky
    )

    if (id.toString().startsWith("7")) {
        return WeatherImage(
            id = id,
            title = titleMap[id] ?: R.string.mist,
            icon = if (icon.endsWith("d")) dayIconsMap[id.toString()[0].toString().toInt()]
                ?: R.drawable.sun_clear_sky else nightIconsMap[id.toString()[0].toString().toInt()]
                ?: R.drawable.night_clear_sky
        )
    }

    else if (id == 800) {
        return WeatherImage(
            id = id,
            title = titleMap[id] ?: R.string.clouds,
            icon = if (icon.endsWith("d")) dayIconsMap[id]
                ?: R.drawable.clouds else nightIconsMap[id] ?: R.drawable.clouds
        )
    }

    return WeatherImage(
        id = id,
        title = titleMap[id.toString()[0].toString().toInt()] ?: R.string.clear,
        icon = if (icon.endsWith("d")) dayIconsMap[id.toString()[0].toString().toInt()]
            ?: R.drawable.sun_clear_sky else nightIconsMap[id.toString()[0].toString().toInt()]
            ?: R.drawable.night_clear_sky
    )
}

fun HourlyForecastNetwork.asModel(timezoneOffset: Int = 0): HourlyForecast {
    return HourlyForecast(
        dateTime = fromUnixDateTimeToFormattedHour(dateTime + timezoneOffset),
        temperature = temperature.roundToInt()
    )
}

fun DailyForecastNetwork.asModel(timezoneOffset: Int = 0): DailyForecast {
    return DailyForecast(
        dateTime = fromUnixDateTimeToFormattedDayAndMonth(dateTime + timezoneOffset),
        temperature = temperature.asModel(),
        weatherImage = weatherImage.map {
            it.asModel()
        }
    )
}

fun TemperaturesNetwork.asModel(): Temperatures {
    return Temperatures(
            minTemperature = minTemperature.roundToInt(),
            maxTemperature = maxTemperature.roundToInt()
    )
}

fun fromUnixDateTimeToFormattedTime(unixDateTime: Long): String {
    val dateTime = Instant.fromEpochSeconds(unixDateTime).toLocalDateTime(TimeZone.UTC)
    return "${dateTime.hour}:${dateTime.minute}"
}

fun fromUnixDateTimeToFormattedHour(unixDateTime: Long): String {
    val dateTime = Instant.fromEpochSeconds(unixDateTime).toLocalDateTime(TimeZone.UTC)
    return "${dateTime.hour}h"
}

fun fromUnixDateTimeToFormattedDayAndMonth(unixDateTime: Long): String {
    val dateTime = Instant.fromEpochSeconds(unixDateTime).toLocalDateTime(TimeZone.UTC)
    return "${dateTime.dayOfMonth}/${dateTime.monthNumber}"
}