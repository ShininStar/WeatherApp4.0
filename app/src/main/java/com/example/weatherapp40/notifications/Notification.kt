package com.example.weatherapp40.notifications

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.weatherapp40.Constants
import com.example.weatherapp40.data.model.WeatherModel

class Notification(private val currentWeather: WeatherModel) {
    private fun notification(context: Context) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentTitle("Прогноз погоды")
            .setContentText("В ${currentWeather.location.name} ${currentWeather.current.temp_c}°C")
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
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, impotance)
            channel.description = description
            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}