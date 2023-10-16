package com.jpmorgan.android.weather.models

data class WeatherData(
    val coord: Coordinates,
    val weather: List<Weather>,
    val base: String,
    val main: MainWeather,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Int,
    val sys: WeatherSys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
) {
}