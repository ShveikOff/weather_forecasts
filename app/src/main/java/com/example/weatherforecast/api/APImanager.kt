package com.example.weatherforecast.api

import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.example.weatherforecast.models.RouteResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class APImanager(private val context: Context) {

    // API ключ OpenRouteService
    private val OPENROUTE_API_KEY = "5b3ce3597851110001cf6248585682bb6bb84bd986e6c6e2a5b66d10"
    private val OPENWEATHER_API_KEY = "86ce19f8df8381eeda67a798434148fd"

    // Базовые URL
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private val OPENROUTE_BASE_URL = "https://api.openrouteservice.org/"

    // Инициализация Retrofit для OpenWeatherMap
    private val retrofitWeather = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherAPI = retrofitWeather.create(WeatherAPI::class.java)

    // Инициализация Retrofit для OpenRouteService
    private val retrofitOpenRoute = Retrofit.Builder()
        .baseUrl(OPENROUTE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val openRouteAPI = retrofitOpenRoute.create(OpenRouteServiceAPI::class.java)

    // Инициализация клиента для получения местоположения
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Получение текущего местоположения
    fun getCurrentLocation(callback: (Location?) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            callback(location)
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
            callback(null)
        }
    }

    // Получение текущей погоды4
    fun getWeather(city: String, callback: (WeatherResponse?) -> Unit) {
        val call = weatherAPI.getCurrentWeather(city, "metric", OPENWEATHER_API_KEY)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                t.printStackTrace()
                callback(null)
            }
        })
    }

    // Получение прогноза на 5 дней
    fun get5DayForecast(city: String, callback: (ForecastResponse?) -> Unit) {
        val call = weatherAPI.get5DayForecast(city, OPENROUTE_API_KEY)

        call.enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                t.printStackTrace()
                callback(null)
            }
        })
    }

    // Получение маршрута между двумя координатами через OpenRouteService
    fun getRoute(
        start: String, // Координаты начальной точки (например, "13.388860,52.517037")
        end: String,   // Координаты конечной точки (например, "13.397634,52.529407")
        callback: (RouteResponse?) -> Unit
    ) {
        val call = openRouteAPI.getRoute(OPENROUTE_API_KEY, start, end)

        call.enqueue(object : Callback<RouteResponse> {
            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                t.printStackTrace()
                callback(null)
            }
        })
    }

    fun getAirQuality(lat: Double, lon: Double, callback: (AirQualityResponse?) -> Unit) {
        val call = weatherAPI.getAirQuality(lat, lon, OPENWEATHER_API_KEY)

        call.enqueue(object : Callback<AirQualityResponse> {
            override fun onResponse(call: Call<AirQualityResponse>, response: Response<AirQualityResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<AirQualityResponse>, t: Throwable) {
                t.printStackTrace()
                callback(null)
            }
        })
    }

    fun getDailyForecast(city: String, callback: (List<DailyForecastItem>?) -> Unit) {
        val call = weatherAPI.getDailyForecast(city, "metric", OPENWEATHER_API_KEY)
        call.enqueue(object : Callback<DailyForecastResponse> {
            override fun onResponse(call: Call<DailyForecastResponse>, response: Response<DailyForecastResponse>) {
                if (response.isSuccessful) {
                    val forecastList = response.body()?.list
                    callback(forecastList)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<DailyForecastResponse>, t: Throwable) {
                t.printStackTrace()
                callback(null)
            }
        })
    }




}
