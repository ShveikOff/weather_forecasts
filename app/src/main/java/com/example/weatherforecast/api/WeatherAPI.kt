package com.example.weatherforecast.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather")
    fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Call<WeatherResponse>

    // Прогноз на 5 дней
    @GET("forecast")
    fun get5DayForecast(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Call<ForecastResponse>

    @GET("forecast/daily")
    fun getDailyForecast(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): Call<DailyForecastResponse>

    @GET("air_pollution")
    fun getAirQuality(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): Call<AirQualityResponse>

    @GET("find")
    fun searchCities(
        @Query("q") query: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Call<CitySearchResponse>

}
