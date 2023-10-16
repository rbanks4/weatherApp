package com.jpmorgan.android.weather

import com.jpmorgan.android.weather.models.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val KEY_TAG = "appid="
const val KEY = "332463c21c93030ae4877f874d710262"
const val WEATHER = "weather"
const val DEFAULT_UNIT = "imperial"

interface Api {
    @GET(WEATHER)
    fun getWeather(
        @Query("appid") key: String = KEY,
        @Query("q") query: String = "",
        @Query("lon") lon:String = "",
        @Query("lat") lat:String? = "",
        @Query("units") unit: String = DEFAULT_UNIT
    ): Call<WeatherData>
}