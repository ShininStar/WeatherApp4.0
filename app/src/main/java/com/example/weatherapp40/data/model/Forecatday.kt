package com.example.weatherapp40.data.model

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.parcelize.Parcelize

@Parcelize
data class Forecatday(
    @Embedded val astro: Astro,
    val date: String,
    val date_epoch: Int,
    @Embedded val day: Day,
    val hour: List<Hour>
) : Parcelable