package com.example.weatherapp40.viewmodel

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import com.example.weatherapp40.Constants.CHANNEL_ID
import com.example.weatherapp40.Constants.startCities
import com.example.weatherapp40.data.model.WeatherModel
import com.example.weatherapp40.data.database.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private var _liveDataMain = MutableLiveData<List<WeatherModel>>()
    val liveDataMain: LiveData<List<WeatherModel>> = _liveDataMain

    private val _liveDataCurrent = MutableLiveData<WeatherModel>()
    val liveDataCurrent: LiveData<WeatherModel> = _liveDataCurrent

    //если в базе данных сохранены предыдущие данные, то берем их, а потом обновляем
    suspend fun getWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.weatherData.isNotEmpty()) {
                _liveDataMain.postValue(repository.weatherData)
                repository.delete()
            }
            _liveDataMain.postValue(repository.getWeather(startCities))
        }
    }

    fun putWeather(weatherModel: WeatherModel) {
        _liveDataCurrent.value = weatherModel
    }

}