package com.example.weatherapp40.data.database
import androidx.room.*
import androidx.room.Dao
import com.example.weatherapp40.data.model.WeatherModel

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherModel(weatherModel: WeatherModel)

    @Query("SELECT * FROM weatherModel")
    fun getWeatherModels() : List<WeatherModel>

    @Query("DELETE FROM weatherModel")
    suspend fun deleteData()

    @Query("SELECT COUNT(*) FROM weatherModel")
    fun getCount(): Int
}