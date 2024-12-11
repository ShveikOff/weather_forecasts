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

    // Изменить метод, чтобы он принимал либо локальное значение пользователя, либо город по выбору пользователя
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
    // Добавить методы на прогноза на 5, 14 дней и месяц
    // Метод который будет с openWeather брать слои температуры, осадков и качества воздуха
    // Вынести для ForeCastMapActivity метод для отображения Google Map основного слоя
    /* Метод который будет взаимодействовать с Google Directions API, вводные данные две координаты
    * он должен вернуть данные для построения маршрута на экране*/

}
