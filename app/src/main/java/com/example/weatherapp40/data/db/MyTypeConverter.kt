package com.example.weatherapp40.data.db

import androidx.room.TypeConverter
import com.example.weatherapp40.data.Forecatday
import com.example.weatherapp40.data.Hour
import com.example.weatherapp40.data.WeatherModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//тайп конвертер для списка дата классов

class MyTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromListForecatdayString(json: String) : List<Forecatday> {
        val listType = object : TypeToken<List<Forecatday>>() {}.type
        return gson.fromJson(json, listType)
    }

    @TypeConverter
    fun toListForecastdayString (forecatday: List<Forecatday>) : String {
        return gson.toJson(forecatday)
    }
}