package com.jpmorgan.android.weather

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jpmorgan.android.weather.models.WeatherData
import com.jpmorgan.android.weather.ui.theme.WeatherTheme
import com.jpmorgan.android.weather.view.WeatherDisplay

private const val QUERY_KEY = "query"
private const val SHARED_PREF_KEY = "sharedPrefKey"
private const val PERMISSION_STATE = 1
class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()
    var pref: SharedPreferences? = null
    var currentLocation: Pair<String, String>? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        initView()
    }

    fun initView() {
        val lastQuery = pref?.getString(QUERY_KEY, "")
        if(lastQuery != null && lastQuery != "") {
            search(lastQuery)
        } else {
            search(currentLocation)
        }
    }

    fun buildView() {
        setContent {
            WeatherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherDisplay.WeatherView(
                        name = "Android ${viewModel.weather?.base}",
                        getWeather = {search(it)},
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    fun search(query: String) {
        ApiCalls.getWeather (
            refresh = { buildView() },
            query = query,
            updateViewModel = { updateWeather(it) }
        )
        saveQuery(query)
    }

    fun search(query: Pair<String, String>?) {
        ApiCalls.getWeather (
            refresh = { buildView() },
            lat = query?.first?:"",
            lon = query?.second?:"",
            updateViewModel = { updateWeather(it) }
        )
    }

    fun updateWeather(data: WeatherData?) {
        viewModel.weather = data
    }

    fun saveQuery(query: String) {
        pref?.let {
            with(it.edit()) {
                putString(QUERY_KEY, query)
                commit()
            }
        }
    }

    fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_STATE)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if(location != null) {
                    currentLocation = Pair(location.latitude.toString(), location.longitude.toString())
                    Log.i("getLastLocation()", "location found: $currentLocation")
                    initView()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_STATE -> {
                getLastLocation()
                Log.i("onRequestPermissionResult()", "location access permitted")
            }
            else -> {
                Toast.makeText(this, "Location access not permitted by user.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}