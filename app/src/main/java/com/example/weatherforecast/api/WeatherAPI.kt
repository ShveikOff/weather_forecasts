package com.example.weatherforecast.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    // Текущая погода
    @GET("weather")
    fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): Call<WeatherResponse>

    // Прогноз на 5 дней
    @GET("forecast")
    fun get5DayForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): Call<ForecastResponse>
}
