package com.example.weatherapptask_1.reporitory

import com.example.weatherapptask_1.helper.RetrofitClient
import com.example.weatherapptask_1.model.WeatherResponse

class WeatherRepository {
    suspend fun getWeatherByCity(city: String, apiKey: String): WeatherResponse {
        return RetrofitClient.api.getWeather(cityName = city, apiKey = apiKey)
    }
}