package com.example.weatherforecast

data class City(
    val name: String,
    var aqi: String,
    val details: String,
    val temperature: String,
    val lat: Double,  // Широта города
    val lon: Double   // Долгота города
)
