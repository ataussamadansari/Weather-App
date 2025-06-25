package com.example.weatherapptask_1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapptask_1.model.WeatherResponse
import com.example.weatherapptask_1.reporitory.AppRepository
import com.example.weatherapptask_1.reporitory.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponse?>()
    val weatherData: LiveData<WeatherResponse?> = _weatherData

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchWeatherByCity(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val result = repository.getWeatherByCity(city, apiKey)
                _weatherData.postValue(result)
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message} : City not found or API failed.")
            }
        }
    }

}