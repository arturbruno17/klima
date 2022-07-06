package com.posart.klima.ui.viewmodels

import android.app.Application
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.*
import com.posart.klima.repositories.WeatherRepository
import com.posart.klima.ui.entities.LatLng
import com.posart.klima.ui.entities.WeatherForecast
import com.posart.klima.ui.entities.asModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    application: Application,
    private val weatherRepository: WeatherRepository
): AndroidViewModel(application) {

    private var _response = MutableLiveData<WeatherForecastResponse>(WeatherForecastResponse.Loading)
    val response : LiveData<WeatherForecastResponse>
        get() = _response

    private var _latLng = MutableLiveData<LatLng>()
    val latLng : LiveData<LatLng>
        get() = _latLng


    fun getWeatherForecast(lat: Double, lon: Double, excludedParts: String, unitSystem: String) {
        viewModelScope.launch {
            try {
                val weatherForecast = weatherRepository.getWeatherForecast(lat, lon, excludedParts, unitSystem)
                checkNotNull(weatherForecast)
                weatherForecast.let {
                    var weatherForecastAsModel: WeatherForecast
                    withContext(Dispatchers.Default) {
                        weatherForecastAsModel = it.asModel(unitSystem)
                        _response.postValue(WeatherForecastResponse.Success(weatherForecastAsModel))
                    }
                }
            } catch (e: Exception) {
                _response.postValue(WeatherForecastResponse.Error)
            }
        }
    }

    fun getLatLngFromLocation(location: String) {
        viewModelScope.launch {
            try {
                var address: List<Address>
                withContext(Dispatchers.IO) {
                     address = Geocoder(getApplication<Application?>().applicationContext).getFromLocationName(location, 1)
                }
                _latLng.postValue(
                    LatLng(address[0].latitude, address[0].longitude)
                )
            } catch (e: Exception) {
                return@launch
            }
        }
    }

    sealed class WeatherForecastResponse {
        object Loading : WeatherForecastResponse()
        data class Success(val weatherForecast: WeatherForecast) : WeatherForecastResponse()
        object Error : WeatherForecastResponse()
    }
}