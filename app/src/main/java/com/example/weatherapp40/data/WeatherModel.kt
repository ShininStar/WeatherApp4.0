package com.example.weatherapp40.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//дата класс с который получаем с сервера, в котором содержатся все остальные дата классы

@Entity(tableName = "weatherModel")
@Parcelize
data class WeatherModel(
    @Embedded
    val current: Current,
    @Embedded
    val forecast: Forecast,
    @Embedded
    val location: Location,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
) : Parcelable
