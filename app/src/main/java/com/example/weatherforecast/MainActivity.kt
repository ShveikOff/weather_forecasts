package com.example.weatherforecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.ImageView
import com.example.weatherforecast.api.APImanager
import com.example.weatherforecast.api.WeatherResponse
import android.content.Intent
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Проверяем, прошел ли пользователь опрос
        val isQuizCompleted = getQuizCompletionStatus()
        if (!isQuizCompleted) {
            // Если нет, запускаем UserQuizActivity
            val intent = Intent(this, UserQuizActivity::class.java)
            startActivityForResult(intent, QUIZ_REQUEST_CODE)
        }

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

        // Кнопка "Map"
        val mapButton: Button = findViewById(R.id.button_map)
        mapButton.setOnClickListener {
            val intent = Intent(this, ForecastMapActivity::class.java)
            startActivity(intent)
        }

        // Кнопка "Reccomendation"
        val reccButton: Button = findViewById(R.id.recommendation)
        reccButton.setOnClickListener {
            val intent = Intent(this, RecomendationActivity::class.java)
            startActivity(intent)
        }


        // Найти кнопку по ID
        val addButton: ImageView = findViewById(R.id.addButton)

        // Установить слушатель для переключения на CityChooseActivity
        addButton.setOnClickListener {
            val intent = Intent(this, CityChooseActivity::class.java)
            startActivity(intent)
        }

        // Найти кнопку по ID
        val settingsButton: ImageView = findViewById(R.id.settingsButton)

        // Установить слушатель для переключения на SettingsActivity
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == QUIZ_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Пользователь завершил опрос
                saveQuizCompletionStatus(true)
            } else {
                // Пользователь выбрал "Пропустить"
                saveQuizCompletionStatus(false)
            }
        }
    }

    private fun getQuizCompletionStatus(): Boolean {
        // Замените на реальную логику для проверки (например, SharedPreferences)
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("quiz_completed", false)
    }

    private fun saveQuizCompletionStatus(completed: Boolean) {
        // Сохраняем статус прохождения опроса
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("quiz_completed", completed)
        editor.apply()
    }

    companion object {
        private const val QUIZ_REQUEST_CODE = 1001
    }
}
