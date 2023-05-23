package com.example.weatherapp40.data.db.repository

import com.example.weatherapp40.data.WeatherModel
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    val weatherData: List<WeatherModel>
    suspend fun insertWeather (weatherModel: WeatherModel)
    suspend fun getWeather(listCities: MutableList<String>): List<WeatherModel>
    suspend fun delete()
}