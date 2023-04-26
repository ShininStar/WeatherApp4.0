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
import com.example.weatherapp40.Constants.REPOSITORY
import com.example.weatherapp40.Constants.startCities
import com.example.weatherapp40.data.WeatherModel
import com.example.weatherapp40.model.apis.WeatherAPI
import com.example.weatherapp40.data.db.MainDb
import com.example.weatherapp40.data.db.repository.WeatherRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var _liveDataMain = MutableLiveData<List<WeatherModel>>()
    val liveDataMain: LiveData<List<WeatherModel>> = _liveDataMain

    private val _liveDataCurrent = MutableLiveData<WeatherModel>()
    val liveDataCurrent: LiveData<WeatherModel> = _liveDataCurrent

    fun initDataBase(context: Context) {
        val mainDb = MainDb.getDb(context)
        REPOSITORY = WeatherRepositoryImpl(WeatherAPI.create(), mainDb)
    }

    //если в базе данных сохранены предыдущие данные, то берем их, а потом обновляем
    suspend fun getWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            if (REPOSITORY.weatherData.isNotEmpty()) {
                _liveDataMain.postValue(REPOSITORY.weatherData)
                REPOSITORY.delete()
            }
            _liveDataMain.postValue(REPOSITORY.getWeather(startCities))
        }
    }

    fun putWeather(weatherModel: WeatherModel) {
        _liveDataCurrent.value = weatherModel
    }

    //создаем уведомление
    private fun notification(context: Context) {
        val notificationManager = getSystemService(context, NotificationManager::class.java) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentTitle("Прогноз погоды")
            .setContentText("В ${liveDataCurrent.value?.location?.name} ${liveDataCurrent.value?.current?.temp_c}°C")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notification: Notification = builder.build()
        notificationManager.notify(1, notification)
    }

    //запускаем уведомление каждый час
    fun scheduleNotificationUpdate(context: Context) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            notification(context)
            scheduleNotificationUpdate(context)
        }, 60 * 60 * 1000)
    }

    //канал уведомлений
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val description = "My Channel Description"
            val impotance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, impotance)
            channel.description = description
            val notificationManager = getSystemService(context, NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}