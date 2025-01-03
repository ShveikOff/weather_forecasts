package com.example.weatherforecast.api

import android.content.Context
import android.location.Location
import android.util.Log
import com.example.weatherforecast.City
import com.example.weatherforecast.models.RouteResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object APImanager {

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

    private var fusedLocationClient: FusedLocationProviderClient? = null

    fun initialize(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    // Получение текущего местоположения
    fun getCurrentLocation(callback: (Location?) -> Unit) {
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            callback(location)
        }?.addOnFailureListener { exception ->
            exception.printStackTrace()
            callback(null)
        } ?: callback(null)
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
                    if (forecastList.isNullOrEmpty()) {
                        Log.e("Forecast", "Forecast list is empty")
                    } else {
                        Log.d("Forecast", "Received forecast data: $forecastList")
                    }
                    callback(forecastList)
                } else {
                    Log.e("Forecast", "API response error: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<DailyForecastResponse>, t: Throwable) {
                t.printStackTrace()
                callback(null)
            }
        })
    }

    fun searchCities(query: String, callback: (List<City>?) -> Unit) {
        Log.d("APIManager", "Searching cities for query: $query") // Лог начала запроса

        val call = weatherAPI.searchCities(query,"metric", OPENWEATHER_API_KEY)
        call.enqueue(object : Callback<CitySearchResponse> {
            override fun onResponse(call: Call<CitySearchResponse>, response: Response<CitySearchResponse>) {
                if (response.isSuccessful) {
                    Log.d("APIManager", "City search successful, response code: ${response.code()}") // Лог успешного ответа

                    val cities = response.body()?.list?.map {
                        City(
                            name = it.name,
                            aqi = "N/A", // AQI будет обновлен позже
                            details = "",
                            temperature = "${it.main.temp.toInt()}°",
                            lat = it.coord.lat, // Координаты из ответа API
                            lon = it.coord.lon
                        )
                    }
                    Log.d("APIManager", "Cities mapped: $cities") // Лог преобразованных данных
                    callback(cities)
                } else {
                    Log.e("APIManager", "City search failed, error code: ${response.code()}") // Лог ошибки с кодом ответа
                    callback(null)
                }
            }

            override fun onFailure(call: Call<CitySearchResponse>, t: Throwable) {
                Log.e("APIManager", "City search request failed: ${t.message}", t) // Лог ошибки запроса
                callback(null)
            }
        })
    }


}