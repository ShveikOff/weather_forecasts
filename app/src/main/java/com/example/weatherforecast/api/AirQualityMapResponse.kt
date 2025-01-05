package com.example.weatherforecast.api

data class AirQualityMapResponse(
    val list: List<AirQualityMapData>
)

data class AirQualityMapData(
    val coord: Coord,
    val main: AirQualityMain
)

data class AirQualityMapMain(
    val aqi: Int
)
