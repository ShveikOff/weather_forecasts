package com.example.weatherforecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        // Отображение текущего местоположения пользователя
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

    private fun addWeatherMarker(latLng: LatLng, weatherInfo: String) {
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Weather Info")
                .snippet(weatherInfo)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        )?.showInfoWindow()
    }

    // Временная функция для получения информации о погоде (заглушка)
    private suspend fun getWeatherInfo(lat: Double, lon: Double): String? {
        // Здесь будет вызов к API для получения данных о погоде
        // Сейчас возвращаем заглушку
        return "Temperature: 15°C, Clear Sky"
    }
}
