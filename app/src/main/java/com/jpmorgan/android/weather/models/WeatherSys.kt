package com.jpmorgan.android.weather.models

data class WeatherSys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Int,
    val sunset: Int
)
