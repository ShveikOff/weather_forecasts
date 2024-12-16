package com.example.weatherforecast.models

data class RouteResponse(
    val routes: List<Route> // Список маршрутов
)

data class Route(
    val summary: Summary,  // Сводная информация
    val geometry: String   // Координаты маршрута (полилиния)
)

data class Summary(
    val distance: Double, // Дистанция в метрах
    val duration: Double  // Время в секундах
)
