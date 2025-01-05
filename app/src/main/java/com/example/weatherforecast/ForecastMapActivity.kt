package com.example.weatherforecast

import android.os.Bundle
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
import com.google.android.gms.maps.model.Marker
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
}
