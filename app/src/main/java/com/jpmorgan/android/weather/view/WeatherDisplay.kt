package com.jpmorgan.android.weather.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jpmorgan.android.weather.MainViewModel

class WeatherDisplay {
    companion object {
        @Composable
        fun WeatherView(name: String, getWeather: (String)->Unit, viewModel: MainViewModel?) {

            var textState by remember { mutableStateOf("") }
            val weather = viewModel?.weather
            val mainTemp = weather?.main
            val temp = mainTemp?.temp?.toString() ?: ""
            var high = mainTemp?.temp_max?: ""
            var low = mainTemp?.temp_min?: ""
            var feelsLike = mainTemp?.feels_like?: ""
            Column(modifier = Modifier.padding(10.dp)) {
                Row(modifier = Modifier.padding(all = 8.dp)) {

                    //Search Bar
                    TextField(
                        value = textState,
                        enabled = true,
                        onValueChange = { textState = it },
                        readOnly = false,
                        modifier = Modifier.fillMaxWidth(0.70f),
                        supportingText = { Text("Please enter query") }
                    )
                    //Set button
                    Button(
                        onClick = {
                            getWeather(textState)
                        }
                    ) {
                        Text(text = "Search")
                    }
                }
                if(temp != "") {
                    Row() {
                        //Contents
                        Text(
                            text = "$temp°",
                            fontSize = 80.sp
                        )
                    }
                }
                if(high != "" && low != "" && feelsLike != "") {
                    Row() {
                        Text(
                            text = "High: $high | Low: $low | Feels Like: $feelsLike",
                            fontSize = 20.sp
                        )
                    }
                }
                Row() {
                    Text(
                        text = viewModel?.weather?.weather?.first()?.description?:"",
                        fontSize = 20.sp
                    )
                }
                //Location
                Row() {
                    Text(
                        text = "Location: " + viewModel?.weather?.name?:"",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }

        @Preview(showBackground = true)
        @Composable
        fun WeatherPreview() {
            WeatherView("Android", {}, null)
        }
    }
}