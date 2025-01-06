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
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.*

class ForecastMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityForecastMapBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val weatherMarkers = mutableListOf<Marker>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("ForecastMapActivity", "onCreate: Initializing components")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.d("ForecastMapActivity", "onCreate: FusedLocationProviderClient initialized")

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        Log.d("ForecastMapActivity", "onCreate: Requesting map async")
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("ForecastMapActivity", "onMapReady: Google Map is ready")
        map = googleMap

        setupMapListeners()

        val selectedCity = FavoriteCitiesRepository.selectedCity.value
        if (selectedCity != null) {
            showCityOnMap(selectedCity.lat, selectedCity.lon, selectedCity.name)
        } else {
            showCurrentLocation()
        }
    }

    private fun setupMapListeners() {
        map.setOnMapLoadedCallback {
            Log.d("ForecastMapActivity", "Map successfully loaded")
        }
        map.setOnMapClickListener { latLng ->
            Log.d("ForecastMapActivity", "Map clicked at $latLng")
            coroutineScope.launch {
                val weatherInfo = getWeatherInfo(latLng.latitude, latLng.longitude)
                if (weatherInfo != null) {
                    Log.d("ForecastMapActivity", "Weather info retrieved: $weatherInfo")
                    addWeatherMarker(latLng, weatherInfo)
                } else {
                    Log.e("ForecastMapActivity", "Failed to retrieve weather info")
                }
            }
        }
        map.setOnCameraIdleListener {
            Log.d("ForecastMapActivity", "Camera is idle")
        }
    }

    private fun showCityOnMap(lat: Double, lon: Double, name: String) {
        val cityLatLng = LatLng(lat, lon)
        Log.d("ForecastMapActivity", "Showing city on map: $name, LatLng: $cityLatLng")
        map.addMarker(
            MarkerOptions()
                .position(cityLatLng)
                .title(name)
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLatLng, 10f))
    }

    private fun showCurrentLocation() {
        Log.d("ForecastMapActivity", "Fetching current location")
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                Log.d("ForecastMapActivity", "Current location: $currentLatLng")
                map.addMarker(
                    MarkerOptions()
                        .position(currentLatLng)
                        .title("Current Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                )
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 10f))
            } else {
                Log.e("ForecastMapActivity", "Location is null")
            }
        }.addOnFailureListener { exception ->
            Log.e("ForecastMapActivity", "Failed to fetch current location", exception)
        }
    }

    private fun addWeatherMarker(latLng: LatLng, weatherInfo: String) {
        Log.d("ForecastMapActivity", "Adding weather marker at $latLng with info: $weatherInfo")
        weatherMarkers.forEach { it.remove() }
        weatherMarkers.clear()

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
            Log.d("ForecastMapActivity", "Fetching weather info for lat: $lat, lon: $lon")
            val result = CompletableDeferred<String?>()
            APImanager.getWeatherByCoordinates(lat, lon) { weatherResponse ->
                if (weatherResponse != null) {
                    val temp = weatherResponse.main.temp
                    val description = weatherResponse.weather.firstOrNull()?.description ?: "No data"
                    Log.d("ForecastMapActivity", "Weather response: Temp: ${temp}°C, Description: $description")
                    result.complete("Temp: ${temp}°C, ${description.capitalize()}")
                } else {
                    Log.e("ForecastMapActivity", "Failed to fetch weather response")
                    result.complete(null)
                }
            }
            result.await()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}

