package com.example.weatherforecast

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        APImanager.initialize(this)

        // Подписка на изменения выбранного города
        FavoriteCitiesRepository.selectedCity.observe(this) { city ->
            if (city != null) {
                cityTextView.text = city.name
                updateAQI(city.lat, city.lon)
                fetchWeatherData(city.name)
            }
        }

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
            fetchLocationAndSetWeather()
        }
    }

    private fun fetchLocationAndSetWeather() {
        if (FavoriteCitiesRepository.selectedCity.value == null) {
            val maxRetries = 3
            var attempt = 0
            val delayBetweenRetries = 1000L // Задержка в миллисекундах (1 секунда)

            fun attemptFetchLocation() {
                APImanager.getCurrentLocation { location ->
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val cityName = addresses[0].locality ?: "Unknown"
                            APImanager.getWeather(cityName) { weatherResponse ->
                                if (weatherResponse != null) {
                                    val city = City(
                                        name = cityName,
                                        aqi = "N/A", // AQI загружается отдельно
                                        details = "",
                                        temperature = "${weatherResponse.main.temp}°C", // Преобразование в строку
                                        lat = location.latitude,
                                        lon = location.longitude
                                    )
                                    FavoriteCitiesRepository.selectCity(city)
                                    updateAQI(location.latitude, location.longitude)
                                    fetchWeatherData(cityName)
                                } else {
                                    Log.e("MainActivity", "Не удалось загрузить данные о погоде")
                                }
                            }
                        } else {
                            Log.e("MainActivity", "Не удалось определить город по координатам")
                        }
                    } else {
                        if (attempt < maxRetries) {
                            attempt++
                            Log.w("MainActivity", "Попытка $attempt из $maxRetries для определения местоположения")
                            Handler(Looper.getMainLooper()).postDelayed(::attemptFetchLocation, delayBetweenRetries)
                        } else {
                            Log.e("MainActivity", "Не удалось определить местоположение после $maxRetries попыток")
                        }
                    }
                }
            }

            attemptFetchLocation()
        } else {
            val city = FavoriteCitiesRepository.selectedCity.value!!
            updateWeatherForCity(city)
        }
    }


    private fun updateWeatherForCity(city: City) {
        cityTextView.text = city.name
        updateAQI(city.lat, city.lon)
        fetchWeatherData(city.name)
    }

    private fun updateAQI(lat: Double, lon: Double) {
        APImanager.getAirQuality(lat, lon) { airQualityResponse ->
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

    // Данный функционал не повзоляет реализовать текущий тарифный план API
    /*
    private fun updateForecast(city: String) {
        apiManager.getDailyForecast(city) { forecastList ->
            runOnUiThread {
                if (forecastList == null || forecastList.isEmpty()) {
                    Log.e("Forecast", "Forecast data is null or empty")
                } else {
                    Log.d("Forecast", "Forecast data: $forecastList")
                }
                if (forecastList != null && forecastList.isNotEmpty()) {
                    val morning = forecastList[0]
                    val afternoon = forecastList[4]
                    val evening = forecastList[8]
                    Log.d("Forecast", "Morning: $morning, Afternoon: $afternoon, Evening: $evening")

                    if (morning.temp == null) {
                        println("Morning temperature is null")
                        Log.e("Forecast", "Temperature data is missing")
                    }

                    if (afternoon.temp == null) {
                        println("Afternoon temperature is null")
                        Log.e("Forecast", "Temperature data is missing")
                    }

                    if (evening.temp == null) {
                        println("Evening temperature is null")
                        Log.e("Forecast", "Temperature data is missing")
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
                } else {
                    Log.e("Forecast", "Forecast data is null or empty")
                }
            }
        }
    } */

    private fun fetchWeatherData(city: String) {
        APImanager.getWeather(city) { weatherResponse ->
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

    // Данный функционал пока не позволяет реализовать текущий тарифный план
    /*
    private fun updateForecastElement(imageViewId: Int, textViewId: Int, iconCode: String, description: String) {
        val imageView = findViewById<ImageView>(imageViewId)
        val textView = findViewById<TextView>(textViewId)

        val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"
        Glide.with(this).load(iconUrl).into(imageView)
        textView.text = description
    }*/

    private fun initializeButtons() {
        findViewById<Button>(R.id.button_extended).setOnClickListener {
            startActivity(Intent(this, ExtendedForecastActivity::class.java))
        }

        findViewById<Button>(R.id.button_map).setOnClickListener {
            startActivity(Intent(this, ForecastMapActivity::class.java))
        }

        findViewById<ImageView>(R.id.addButton).setOnClickListener {
            startActivity(Intent(this, CityChooseActivity::class.java))
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1002
    }
}
