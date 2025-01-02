package com.example.weatherforecast

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.TextView
import android.widget.ImageView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.weatherforecast.api.APImanager
import com.example.weatherforecast.api.DailyForecastItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var cityTextView: TextView
    private val apiManager by lazy { APImanager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        cityTextView = findViewById(R.id.titleLabel)

        checkLocationPermission()
        initializeButtons()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        apiManager.getCurrentLocation { location ->
            if (location != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val city = addresses[0].locality ?: "Unknown"
                    cityTextView.text = city
                    updateAQI(location.latitude, location.longitude)
                    updateForecast(city)
                    fetchWeatherData(city)
                }
            }
        }
    }

    private fun updateAQI(lat: Double, lon: Double) {
        apiManager.getAirQuality(lat, lon) { airQualityResponse ->
            runOnUiThread {
                if (airQualityResponse != null) {
                    val aqi = airQualityResponse.list.firstOrNull()?.main?.aqi
                    val aqiText = when (aqi) {
                        1 -> "Good (AQI: $aqi)"
                        2 -> "Fair (AQI: $aqi)"
                        3 -> "Moderate (AQI: $aqi)"
                        4 -> "Poor (AQI: $aqi)"
                        5 -> "Very Poor (AQI: $aqi)"
                        else -> "Unknown"
                    }
                    findViewById<TextView>(R.id.aqiLabel).text = aqiText
                } else {
                    findViewById<TextView>(R.id.aqiLabel).text = "Failed to load AQI"
                }
            }
        }
    }

    private fun updateForecast(city: String) {
        apiManager.getDailyForecast(city) { forecastList ->
            runOnUiThread {
                if (forecastList != null && forecastList.isNotEmpty()) {
                    val morning = forecastList[0]
                    val afternoon = forecastList[4]
                    val evening = forecastList[8]

                    if (morning.temp == null) {
                        println("Morning temperature is null")
                    }

                    if (afternoon.temp == null) {
                        println("Afternoon temperature is null")
                    }

                    if (evening.temp == null) {
                        println("Evening temperature is null")
                    }

                    // Проверяем, что temp не null, прежде чем обращаться к нему
                    morning.temp?.let { temp ->
                        updateForecastElement(
                            R.id.morningImageView,
                            R.id.morningTextView,
                            morning.weather[0].icon,
                            "${morning.weather[0].description.capitalize()} ${temp.day?.toInt() ?: "N/A"}°"
                        )
                    }

                    afternoon.temp?.let { temp ->
                        updateForecastElement(
                            R.id.afternoonImageView,
                            R.id.afternoonTextView,
                            afternoon.weather[0].icon,
                            "${afternoon.weather[0].description.capitalize()} ${temp.day?.toInt() ?: "N/A"}°"
                        )
                    }

                    evening.temp?.let { temp ->
                        updateForecastElement(
                            R.id.eveningImageView,
                            R.id.eveningTextView,
                            evening.weather[0].icon,
                            "${evening.weather[0].description.capitalize()} ${temp.day?.toInt() ?: "N/A"}°"
                        )
                    }
                }
            }
        }
    }

    private fun fetchWeatherData(city: String) {
        apiManager.getWeather(city) { weatherResponse ->
            if (weatherResponse != null) {
                val temperature = weatherResponse.main.temp
                val description = weatherResponse.weather[0].description
                runOnUiThread {
                    findViewById<TextView>(R.id.temperatureCircle).text =
                        "${temperature}°C\n${description.capitalize()}"
                }
            }
        }
    }


    private fun updateForecastElement(imageViewId: Int, textViewId: Int, iconCode: String, description: String) {
        val imageView = findViewById<ImageView>(imageViewId)
        val textView = findViewById<TextView>(textViewId)

        val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"
        Glide.with(this).load(iconUrl).into(imageView)
        textView.text = description
    }

    private fun initializeButtons() {
        findViewById<Button>(R.id.button_extended).setOnClickListener {
            startActivity(Intent(this, ExtendedForecastActivity::class.java))
        }

        findViewById<Button>(R.id.button_map).setOnClickListener {
            startActivity(Intent(this, ForecastMapActivity::class.java))
        }

        findViewById<Button>(R.id.recommendation).setOnClickListener {
            startActivity(Intent(this, RecomendationActivity::class.java))
        }

        findViewById<ImageView>(R.id.addButton).setOnClickListener {
            startActivity(Intent(this, CityChooseActivity::class.java))
        }

        findViewById<ImageView>(R.id.settingsButton).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1002
    }
}
