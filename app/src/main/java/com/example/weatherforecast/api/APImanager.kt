package com.example.weatherforecast.api

import com.example.weatherforecast.models.RouteResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APImanager {

    // API ключ OpenRouteService
    private val OPENROUTE_API_KEY = "5b3ce3597851110001cf6248585682bb6bb84bd986e6c6e2a5b66d10"

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

    // Получение текущей погоды
    fun getWeather(city: String, callback: (WeatherResponse?) -> Unit) {
        val call = weatherAPI.getCurrentWeather(city, OPENROUTE_API_KEY)

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
}
