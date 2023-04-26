package com.example.weatherapp40

import com.example.weatherapp40.data.db.repository.WeatherRepository

object Constants {
    const val API_KEY = "c01566bdcd7d48f486b83500231303"
    const val BASE_URL = "https://api.weatherapi.com/v1/"
    const val DATA = "data"
    const val CHANNEL_ID = "channel_id"
    lateinit var REPOSITORY: WeatherRepository
    val startCities = mutableListOf(
        "Moscow",
        "Paris",
        "London",
        "Washington",
        "Berlin",
        "Tokyo",
        "Ankara",
        "Minsk",
        "Rome",
        "Beijing",
        "Oslo"
    )
}