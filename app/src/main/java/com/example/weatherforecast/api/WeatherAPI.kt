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

    @GET("weather")
    fun getWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): Call<WeatherResponse>

    // New endpoint for temperature layer data
    @GET("box/city")
    fun getWeatherForArea(
        @Query("bbox") boundingBox: String, // Format: "lon-left,lat-bottom,lon-right,lat-top,zoom"
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): Call<WeatherAreaResponse>

    // New endpoint for precipitation layer data
    @GET("precipitation")
    fun getPrecipitationData(
        @Query("bbox") boundingBox: String, // Format: "lon-left,lat-bottom,lon-right,lat-top,zoom"
        @Query("appid") apiKey: String
    ): Call<PrecipitationResponse>

    // New endpoint for air quality layer data
    @GET("air_pollution")
    fun getAirQualityData(
        @Query("bbox") boundingBox: String, // Format: "lon-left,lat-bottom,lon-right,lat-top,zoom"
        @Query("appid") apiKey: String
    ): Call<AirQualityMapResponse>

}
