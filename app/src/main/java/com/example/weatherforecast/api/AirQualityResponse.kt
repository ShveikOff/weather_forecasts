package com.example.weatherforecast.api

data class AirQualityResponse(
    val list: List<AirQualityData>
)

data class AirQualityData(
    val main: AirQualityMain
)

data class AirQualityMain(
    val aqi: Int // Индекс качества воздуха (1 - хороший, 5 - опасный)
)
