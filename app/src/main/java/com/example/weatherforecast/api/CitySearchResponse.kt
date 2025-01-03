package com.example.weatherforecast.api

data class CitySearchResponse(
    val list: List<CityResult>
)

data class CityResult(
    val id: Int,
    val name: String,
    val main: CityWeatherMain,
    val sys: Sys,
    val coord: Coord
)

data class CityWeatherMain(
    val temp: Float,
    val tempMin: Float,
    val tempMax: Float
)

data class Sys(
    val country: String
)

data class Coord(
    val lat: Double,
    val lon: Double
)
