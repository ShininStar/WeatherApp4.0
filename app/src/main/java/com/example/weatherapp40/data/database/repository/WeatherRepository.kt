package com.example.weatherapp40.data.database.repository

import com.example.weatherapp40.data.model.WeatherModel

interface WeatherRepository {
    val weatherData: List<WeatherModel>
    suspend fun insertWeather (weatherModel: WeatherModel)
    suspend fun getWeather(listCities: MutableList<String>): List<WeatherModel>
    suspend fun delete()
}