package com.jpmorgan.android.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jpmorgan.android.weather.models.WeatherData
private const val WEATHER_KEY = "weather"
private const val LAST_SEARCHED = "lastSearched"
class MainViewModel (private val savedStateHandle: SavedStateHandle): ViewModel() {
    var weather: WeatherData? = null
}