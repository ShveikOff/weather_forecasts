package com.example.weatherforecast.api


import com.example.weatherforecast.models.RouteResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenRouteServiceAPI {
    @GET("v2/directions/driving-car") // Эндпоинт OpenRouteService для машин
    fun getRoute(
        @Query("api_key") apiKey: String,  // Ваш API ключ OpenRouteService
        @Query("start") start: String,    // Начальные координаты: "долгота,широта"
        @Query("end") end: String         // Конечные координаты: "долгота,широта"
    ): Call<RouteResponse>
}
