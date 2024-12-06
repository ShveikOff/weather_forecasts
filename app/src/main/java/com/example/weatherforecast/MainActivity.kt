package com.example.weatherforecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.weatherforecast.api.APImanager
import com.example.weatherforecast.api.WeatherResponse
import android.content.Intent
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Найдите ваш TextView с ID temperatureCircle
        val temperatureTextView: TextView = findViewById(R.id.temperatureCircle)

        // Создайте экземпляр APImanager и запросите погоду для Бишкека
        val cityName = "Bishkek"
        val apiManager = APImanager()

        apiManager.getWeather(cityName) { weatherResponse ->
            if (weatherResponse != null) {
                // Извлеките температуру и описание из ответа
                val temperature = weatherResponse.main.temp
                val description = weatherResponse.weather[0].description

                // Обновите текст в TextView на UI
                runOnUiThread {
                    temperatureTextView.text = "${temperature}°C\n${description.capitalize()}"
                }
            } else {
                // Если запрос не удался, установите текст по умолчанию
                runOnUiThread {
                    temperatureTextView.text = "Failed to load data"
                }
            }
        }

        // Кнопка "Extended"
        val extendedButton: Button = findViewById(R.id.button_extended)
        extendedButton.setOnClickListener {
            val intent = Intent(this, ExtendedForecastActivity::class.java)
            startActivity(intent)
        }
    }
}
