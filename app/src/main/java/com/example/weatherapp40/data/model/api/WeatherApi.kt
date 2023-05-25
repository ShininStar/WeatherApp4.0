package com.example.weatherapp40.data.model.api

import com.example.weatherapp40.Constants.API_KEY
import com.example.weatherapp40.data.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

//создаем интерфейс для запроса на сервер (Retrofit)
interface WeatherApi {
    @GET("forecast.json?key=$API_KEY&days=3&aqi=no&alerts=no")
    suspend fun getWeather(@Query("q") city: String): WeatherModel
}