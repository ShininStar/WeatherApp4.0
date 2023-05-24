package com.example.weatherapp40.data.model.api

import com.example.weatherapp40.Constants.API_KEY
import com.example.weatherapp40.data.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

//создаем интерфейс для запроса на сервер (Retrofit)
interface WeatherApi {
    @GET("forecast.json?key=$API_KEY&days=3&aqi=no&alerts=no")
    suspend fun getWeather(@Query("q") city: String): WeatherModel

//    companion object {
//        fun create() : WeatherAPI {
//            val interceptor = HttpLoggingInterceptor()
//            interceptor.level = HttpLoggingInterceptor.Level.BODY
//            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
//            val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL).client(client)
//                .addConverterFactory(GsonConverterFactory.create()).build()
//            return retrofit.create(WeatherAPI::class.java)
//        }
//    }
}