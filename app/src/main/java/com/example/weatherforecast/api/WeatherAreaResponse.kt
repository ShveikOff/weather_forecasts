package com.example.weatherforecast.api

typealias WeatherAreaResponse = List<WeatherAreaData>

data class WeatherAreaData(
    val coord: WeatherAreaDataCoord,
    val main: WeatherAreaDataMain
)

data class WeatherAreaDataCoord(
    val lat: Double,
    val lon: Double
)

data class WeatherAreaDataMain(
    val temp: Float
)
