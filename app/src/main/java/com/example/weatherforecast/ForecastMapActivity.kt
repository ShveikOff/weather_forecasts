package com.example.weatherforecast

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecast.api.APImanager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.example.weatherforecast.databinding.ActivityForecastMapBinding
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForecastMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityForecastMapBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация клиента для получения местоположения
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Инициализация карты
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val selectedCity = FavoriteCitiesRepository.selectedCity.value
        if (selectedCity != null) {
            val cityLatLng = LatLng(selectedCity.lat, selectedCity.lon)
            map.addMarker(
                MarkerOptions()
                    .position(cityLatLng)
                    .title(selectedCity.name)
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLatLng, 10f))
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    map.addMarker(
                        MarkerOptions()
                            .position(currentLatLng)
                            .title("Current Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    )
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 10f))
                }
            }
        }

        // Обработчик нажатий на карту для отображения погоды в выбранной точке
        map.setOnMapClickListener { latLng ->
            CoroutineScope(Dispatchers.IO).launch {
                val weatherInfo = getWeatherInfo(latLng.latitude, latLng.longitude)
                withContext(Dispatchers.Main) {
                    if (weatherInfo != null) {
                        addWeatherMarker(latLng, weatherInfo)
                    }
                }
            }
        }
    }

    private val weatherMarkers = mutableListOf<Marker>()

    private fun addWeatherMarker(latLng: LatLng, weatherInfo: String) {
        // Удаляем все предыдущие маркеры погоды
        weatherMarkers.forEach { it.remove() }
        weatherMarkers.clear()

        // Добавляем новый маркер
        val marker = map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Weather Info")
                .snippet(weatherInfo)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        )
        marker?.showInfoWindow()
        marker?.let { weatherMarkers.add(it) }
    }

    private suspend fun getWeatherInfo(lat: Double, lon: Double): String? {
        return withContext(Dispatchers.IO) {
            val result = CompletableDeferred<String?>()
            APImanager.getWeatherByCoordinates(lat, lon) { weatherResponse ->
                if (weatherResponse != null) {
                    val temp = weatherResponse.main.temp
                    val description = weatherResponse.weather.firstOrNull()?.description ?: "No data"
                    result.complete("Temp: ${temp}°C, ${description.capitalize()}")
                } else {
                    result.complete(null)
                }
            }
            result.await()
        }
    }

    // это не работает из-за ограничений тарифного плана
    /*
    private fun showLayerSelectionDialog() {
        val layers = arrayOf("Temperature", "Precipitation", "Air Quality")
        val checkedItem = -1

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Select Layer")
            .setSingleChoiceItems(layers, checkedItem) { dialog, which ->
                when (which) {
                    0 -> applyLayer("temperature")
                    1 -> applyLayer("precipitation")
                    2 -> applyLayer("air_quality")
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun applyLayer(layer: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val bounds = map.projection.visibleRegion.latLngBounds // Переносим вызов на основной поток
            when (layer) {
                "temperature" -> {
                    APImanager.getTemperatureLayerData(bounds) { data ->
                        if (data != null) {
                            val style = createDynamicStyle(data.toMap(), "#FF0000", "#0000FF")
                            CoroutineScope(Dispatchers.Main).launch {
                                map.setMapStyle(MapStyleOptions(style))
                            }
                        } else {
                            Log.e("ForecastMapActivity", "Failed to fetch temperature layer data")
                        }
                    }
                }
                "precipitation" -> {
                    APImanager.getPrecipitationLayerData(bounds) { data ->
                        if (data != null) {
                            val style = createDynamicStyle(data.toMap(), "#FFFFFF", "#0000FF")
                            CoroutineScope(Dispatchers.Main).launch {
                                map.setMapStyle(MapStyleOptions(style))
                            }
                        } else {
                            Log.e("ForecastMapActivity", "Failed to fetch precipitation layer data")
                        }
                    }
                }
                "air_quality" -> {
                    APImanager.getAirQualityLayerData(bounds) { data ->
                        if (data != null) {
                            val convertedData = data.map { it.first to it.second.toFloat() }.toMap()
                            val style = createDynamicStyle(convertedData.toMap(), "#00FF00", "#FF0000")
                            CoroutineScope(Dispatchers.Main).launch {
                                map.setMapStyle(MapStyleOptions(style))
                            }
                        } else {
                            Log.e("ForecastMapActivity", "Failed to fetch air quality layer data")
                        }
                    }
                }
            }
        }
    }
    */


    private fun createDynamicStyle(data: Map<LatLng, Float>, colorStart: String, colorEnd: String): String {
        val gradient = generateGradient(colorStart, colorEnd, data.values.minOrNull() ?: 0f, data.values.maxOrNull() ?: 1f)
        val styleBuilder = StringBuilder("[")

        for ((latLng, value) in data) {
            val color = gradient(value)
            styleBuilder.append("""
            {
                "featureType": "all",
                "stylers": [
                    { "color": "$color" }
                ],
                "geometry": {
                    "location": {"lat": ${latLng.latitude}, "lng": ${latLng.longitude}},
                    "radius": ${value * 100} // Пример: радиус зависит от значения
                }
            },
        """.trimIndent())
        }

        styleBuilder.append("]")
        return styleBuilder.toString()
    }

    private fun generateGradient(colorStart: String, colorEnd: String, minValue: Float, maxValue: Float): (Float) -> String {
        return { value ->
            val ratio = ((value - minValue) / (maxValue - minValue)).coerceIn(0f, 1f)
            // Пример: рассчитываем цвет между начальным и конечным
            interpolateColor(colorStart, colorEnd, ratio)
        }
    }

    private fun interpolateColor(start: String, end: String, ratio: Float): String {
        val startColor = Color.parseColor(start)
        val endColor = Color.parseColor(end)
        val red = (Color.red(startColor) + ratio * (Color.red(endColor) - Color.red(startColor))).toInt()
        val green = (Color.green(startColor) + ratio * (Color.green(endColor) - Color.green(startColor))).toInt()
        val blue = (Color.blue(startColor) + ratio * (Color.blue(endColor) - Color.blue(startColor))).toInt()
        return String.format("#%02X%02X%02X", red, green, blue)
    }

}
