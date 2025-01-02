package com.example.weatherforecast.api

data class DailyForecastItem(
    val dt: Long, // Временная метка прогноза
    val temp: Temp?, // Объект температур (день, ночь и т.д.)
    val weather: List<WeatherDetail> // Список погодных условий (иконки, описание)
)

data class Temp(
    val day: Float?,
    val min: Float?,
    val max: Float?,
    val night: Float?,
    val eve: Float?,
    val morn: Float?
)

data class WeatherDetail(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
