package com.example.weatherforecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.content.Intent
import android.widget.Button
import com.example.weatherforecast.api.APImanager
import com.example.weatherforecast.api.WeatherResponse

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val temperatureTextView: TextView = findViewById(R.id.temperatureCircle)
        val cityName = "Bishkek"
        val apiManager = APImanager()

        apiManager.getWeather(cityName) { weatherResponse ->
            if (weatherResponse != null) {
                val temperature = weatherResponse.main.temp
                val description = weatherResponse.weather[0].description

                runOnUiThread {
                    temperatureTextView.text = "${temperature}°C\n${description.capitalize()}"
                }
            } else {
                runOnUiThread {
                    temperatureTextView.text = "Failed to load data"
                }
            }
        }

        findViewById<Button>(R.id.button_extended).setOnClickListener {
            startActivity(Intent(this, ExtendedForecastActivity::class.java))
        }

        findViewById<Button>(R.id.button_map).setOnClickListener {
            startActivity(Intent(this, ForecastMapActivity::class.java))
        }
    }
}
