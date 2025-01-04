package com.example.weatherforecast

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Spinner
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.weatherforecast.api.APImanager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ExtendedForecastActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extended_forecast)
        APImanager.initialize(this)

        val cityName = "Bishkek" // Здесь можно динамически подставить выбранный город.
        fetchForecast(cityName)
    }

    private fun fetchForecast(city: String) {
        APImanager.get5DayForecast(city) { forecastResponse ->
            if (forecastResponse != null) {
                val dataPoints = forecastResponse.list.map { forecastItem ->
                    Pair(forecastItem.dt_txt, forecastItem.main.temp.toDouble())
                }
                updateChart(dataPoints)
            } else {
                Log.e("ExtendedForecast", "Failed to fetch forecast data")
            }
        }
    }

    private fun updateChart(dataPoints: List<Pair<String, Double>>) {
        val entries = dataPoints.mapIndexed { index, data ->
            Entry(index.toFloat(), data.second.toFloat())
        }
        val dataSet = LineDataSet(entries, "Temperature")
        dataSet.color = ContextCompat.getColor(this, R.color.purple_500)
        dataSet.valueTextColor = ContextCompat.getColor(this, R.color.black)
        val lineData = LineData(dataSet)

        findViewById<LineChart>(R.id.forecastChart).apply {
            data = lineData
            invalidate()
        }
    }
}
