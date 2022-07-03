package com.posart.klima.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posart.klima.data.remote.entities.WeatherForecast
import com.posart.klima.repositories.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository = WeatherRepository()
): ViewModel() {

    private var _response = MutableLiveData<WeatherForecastResponse>(WeatherForecastResponse.Loading)
    val response : LiveData<WeatherForecastResponse>
        get() = _response


    fun getWeatherForecast(lat: Double, lon: Double, excludedParts: String) {
        viewModelScope.launch {
            try {
                val weatherForecast = weatherRepository.getWeatherForecast(lat, lon, excludedParts)
                checkNotNull(weatherForecast)
                weatherForecast.let {
                    _response.postValue(WeatherForecastResponse.Success(it))
                }
            } catch (e: Exception) {
                _response.postValue(WeatherForecastResponse.Error)
            }
        }
    }

    sealed class WeatherForecastResponse {
        object Loading : WeatherForecastResponse()
        data class Success(val weatherForecast: WeatherForecast) : WeatherForecastResponse()
        object Error : WeatherForecastResponse()
    }
}