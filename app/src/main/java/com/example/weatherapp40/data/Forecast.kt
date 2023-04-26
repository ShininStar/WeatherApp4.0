package com.example.weatherapp40.data

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Forecast(
    val forecastday: List<Forecatday>
) : Parcelable