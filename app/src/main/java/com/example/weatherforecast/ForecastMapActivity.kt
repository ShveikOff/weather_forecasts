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

        // Инициализация клиента для работы с местоположением
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Инициализация карты
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Настройка текущего местоположения
        displayUserLocation()

        // Обработчик нажатий на карту для отображения информации о погоде
        map.setOnMapClickListener { latLng ->
            CoroutineScope(Dispatchers.IO).launch {
                val weatherInfo = fetchWeatherInfo(latLng.latitude, latLng.longitude)
                withContext(Dispatchers.Main) {
                    weatherInfo?.let {
                        addWeatherMarker(latLng, it)
                    }
                }
            }
        }
    }

    /**
     * Отображает текущее местоположение пользователя на карте
     */
    private fun displayUserLocation() {
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

    /**
     * Добавляет маркер с информацией о погоде на карту
     */
    private fun addWeatherMarker(latLng: LatLng, weatherInfo: String) {
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Weather Info")
                .snippet(weatherInfo)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        )?.showInfoWindow()
    }

    /**
     * Получает информацию о погоде по координатам
     * (на данный момент используется заглушка)
     */
    private suspend fun fetchWeatherInfo(lat: Double, lon: Double): String? {
        // Здесь можно подключить реальный API для получения данных
        // Для демонстрации возвращается тестовая информация
        return "Temperature: 15°C, Clear Sky"
    }
}
