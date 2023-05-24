package com.example.weatherapp40.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Forecast(
    val forecastday: List<Forecatday>
) : Parcelable