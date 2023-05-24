package com.example.weatherapp40.data.database.repository


import com.example.weatherapp40.data.model.WeatherModel
import com.example.weatherapp40.data.database.Dao
import com.example.weatherapp40.data.model.api.WeatherApi
import javax.inject.Inject

//класс для соединения базы даннхы и weatherApi

class WeatherRepositoryImpl @Inject constructor(
    private val weatherAPI: WeatherApi,
    private val dao: Dao
    ) : WeatherRepository {
    override val weatherData: List<WeatherModel>
        get() = dao.getWeatherModels()

    override suspend fun insertWeather(weatherModel: WeatherModel) {
        dao.insertWeatherModel(weatherModel)
    }

    override suspend fun getWeather(listCities: MutableList<String>): List<WeatherModel> {
        for (city in listCities) {
            val weather = weatherAPI.getWeather(city)
            dao.insertWeatherModel(weather)
        }
        return dao.getWeatherModels()
    }

    override suspend fun delete() {
        dao.deleteData()
    }
}

//class WeatherRepositoryImpl(private val weatherAPI: WeatherAPI, private val mainDb: MainDb) : WeatherRepository {
//    override val weatherData: List<WeatherModel>
//        get() = mainDb.getDao().getWeatherModels()
//
//    override suspend fun insertWeather(weatherModel: WeatherModel) {
//        mainDb.getDao().insertWeatherModel(weatherModel)
//    }
//
//    override suspend fun getWeather(listCities: MutableList<String>): List<WeatherModel> {
//        val dao = mainDb.getDao()
//            for (city in listCities) {
//                val weather = weatherAPI.getWeather(city)
//                dao.insertWeatherModel(weather)
//            }
//            return dao.getWeatherModels()
//    }
//
//    override fun delete() {
//        mainDb.getDao().deleteData()
//    }
//}