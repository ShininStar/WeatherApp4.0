package com.example.weatherapp40.data.database

import androidx.room.*
import com.example.weatherapp40.data.model.WeatherModel

//класс базы даннх Room

@Database(entities = [WeatherModel::class], version = 1)
@TypeConverters(MyTypeConverter::class)
abstract class MainDb : RoomDatabase() {
    abstract val dao: Dao
}