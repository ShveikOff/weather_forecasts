package com.example.weatherforecast.api

data class WeatherResponse(
    val name: String, // Название города
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Float, // Температура
    val humidity: Int // Влажность
)

data class Weather(
    val description: String // Описание погоды
)
