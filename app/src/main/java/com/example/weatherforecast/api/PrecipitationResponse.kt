package com.example.weatherforecast.api

data class PrecipitationResponse(
    val list: List<PrecipitationData>
)

data class PrecipitationData(
    val coord: Coord,
    val precipitation: Precipitation
)

data class Precipitation(
    val volume: Float
)
