package com.example.weatherforecast.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APImanager {

    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private val API_KEY = "86ce19f8df8381eeda67a798434148fd" // Замените на ваш API-ключ OpenWeatherMap

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherAPI = retrofit.create(WeatherAPI::class.java)

    fun getWeather(city: String, callback: (WeatherResponse?) -> Unit) {
        val call = weatherAPI.getCurrentWeather(city, API_KEY)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                t.printStackTrace()
                callback(null)
            }
        })
    }
}
