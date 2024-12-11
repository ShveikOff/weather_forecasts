package com.example.weatherforecast

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecomendationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendation)

        // Инициализация элементов
        val recommendationText: TextView = findViewById(R.id.recommendationText)
        val weatherDetails: TextView = findViewById(R.id.weatherDetails)
        val acceptButton: Button = findViewById(R.id.acceptButton)
        val refreshButton: Button = findViewById(R.id.refreshButton)

        // Пример данных
        val recommendations = "🌂 Take an umbrella\nChance of rain in the evening\n\n" +
                " • Check the condition of the windshield wipers before leaving.\n" +
                " • Roads may be slippery, please maintain a safe distance."
        val weatherInfo = "• Temperature: 22°C\n• Humidity: 60%\n• Wind: 15 km/h"

        // Установка текста
        recommendationText.text = recommendations
        weatherDetails.text = weatherInfo

        // Обработчик кнопки "Принять рекомендацию"
        acceptButton.setOnClickListener {
            // Здесь можно добавить логику для принятия рекомендаций
        }

        // Обработчик кнопки "Обновить"
        refreshButton.setOnClickListener {
            // Здесь можно добавить логику для обновления данных
        }
    }
}
