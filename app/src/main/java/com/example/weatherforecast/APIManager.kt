package com.example.weatherforecast

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class APIManager(private val apiKey: String) {
    private val client = OkHttpClient()
    private val baseUrl = "https://api.openweathermap.org/data/2.5/onecall"

    fun getCoordinates(cityName: String): Pair<Double, Double>? {
        val geocodeUrl = "http://api.openweathermap.org/geo/1.0/direct"
        val requestUrl = "$geocodeUrl?q=$cityName&limit=1&appid=$apiKey"

        val request = Request.Builder()
            .url(requestUrl)
            .build()

        client.newCall(request).execute().use { response ->
            return if (response.isSuccessful) {
                val responseData = response.body?.string()
                if (responseData != null) {
                    val jsonArray = JSONArray(responseData)
                    if (jsonArray.length() > 0) {
                        val jsonObject = jsonArray.getJSONObject(0)
                        val lat = jsonObject.getDouble("lat")
                        val lon = jsonObject.getDouble("lon")
                        Pair(lat, lon)
                    } else {
                        println("No coordinates found for city: $cityName")
                        null
                    }
                } else {
                    println("Empty response body for coordinates request")
                    null
                }
            } else {
                println("Error fetching coordinates: ${response.message}")
                null
            }
        }
    }

    fun getWeekWeather(cityName: String) {
        val coordinates = getCoordinates(cityName)
        if (coordinates == null) {
            println("Failed to get coordinates for city: $cityName")
            return
        }

        val (lat, lon) = coordinates
        val requestUrl = "$baseUrl?lat=$lat&lon=$lon&exclude=minutely,hourly&units=metric&appid=$apiKey"

        val request = Request.Builder()
            .url(requestUrl)
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val responseData = response.body?.string()
                if (responseData != null) {
                    val json = JSONObject(responseData)
                    val dailyArray = json.getJSONArray("daily")

                    for (i in 0 until dailyArray.length()) {
                        val dayData = dailyArray.getJSONObject(i)
                        val timestamp = dayData.getLong("dt")
                        val temperature = dayData.getJSONObject("temp").getDouble("day")
                        val weatherDescription = dayData.getJSONArray("weather").getJSONObject(0).getString("description")

                        println("Date (timestamp): $timestamp, Temperature: $temperature°C, Description: $weatherDescription")
                    }
                } else {
                    println("Empty response body for weather request")
                }
            } else {
                println("Error fetching weekly weather data: ${response.message}")
            }
        }
    }
}

fun main() {
    val apiKey = "YOUR_API_KEY_HERE" // Замените на ваш API ключ от OpenWeather
    val apiManager = APIManager(apiKey)

    // Получаем данные о погоде на неделю для города Бишкек
    apiManager.getWeekWeather("Bishkek")
}
