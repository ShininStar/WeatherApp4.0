package com.example.weatherapp40.model.apis

import com.example.weatherapp40.Constants.API_KEY
import com.example.weatherapp40.Constants.BASE_URL
import com.example.weatherapp40.data.WeatherModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//создаем интерфейс для запроса на сервер (Retrofit)
interface WeatherAPI {
    @GET("forecast.json?key=$API_KEY&days=3&aqi=no&alerts=no")
    suspend fun getWeather(@Query("q") city: String): WeatherModel

    companion object {
        fun create() : WeatherAPI {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
            return retrofit.create(WeatherAPI::class.java)
        }
    }
}