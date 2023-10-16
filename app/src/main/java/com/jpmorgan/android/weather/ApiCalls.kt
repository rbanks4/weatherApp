package com.jpmorgan.android.weather

import android.util.Log
import com.jpmorgan.android.weather.models.WeatherData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCalls {
    companion object {
        fun getWeather(
            refresh: () -> Unit,
            query: String,
            updateViewModel: (WeatherData?) -> Unit
        ) {
            val TAG = "getWeather()"
            Log.i(TAG, "query $query")
            val call = ApiClient.apiService.getWeather(query = query)
            makeCall(call, refresh = { refresh() }, updateViewModel = { updateViewModel(it) })
        }

        fun getWeather(
            refresh: () -> Unit,
            lat: String,
            lon: String,
            updateViewModel: (WeatherData?) -> Unit
        ) {
            val TAG = "getWeather()"
            Log.i(TAG, "query lat $lat lon $lon")
            val call = ApiClient.apiService.getWeather(lat = lat, lon = lon)
            makeCall(call, refresh = { refresh() }, updateViewModel = { updateViewModel(it) })
        }

        fun makeCall(
            call: Call<WeatherData>,
            refresh: () -> Unit,
            updateViewModel: (WeatherData?) -> Unit
        ) {
            val TAG = "makeCall()"
            call.enqueue(object : Callback<WeatherData> {
                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    if (response.isSuccessful) {
                        Log.i(TAG, "success: ${response.body()}")
                        updateViewModel(response.body())
                        refresh()
                    } else {
                        Log.i(TAG, "response not successful: ${response.message()}")
                        refresh()
                    }
                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    Log.e(TAG, "failure: ${t.message}")
                    refresh()
                }
            })
        }
    }
}