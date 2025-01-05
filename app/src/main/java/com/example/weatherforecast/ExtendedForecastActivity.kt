package com.example.weatherforecast

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.weatherforecast.api.APImanager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import com.github.mikephil.charting.formatter.ValueFormatter


class ExtendedForecastActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extended_forecast)
        APImanager.initialize(this)

        // Подписка на изменения выбранного города
        FavoriteCitiesRepository.selectedCity.observe(this) { city ->
            if (city != null) {
                fetchForecast(city.name)
            } else {
                Log.e("ExtendedForecast", "Selected city is null")
            }
        }

        initializeButtons()
    }

    private fun fetchForecast(city: String) {
        APImanager.get5DayForecast(city) { forecastResponse ->
            if (forecastResponse != null) {
                val groupedData = forecastResponse.list.groupBy {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(it.dt_txt)!!
                    )
                }

                // Ограничиваем данные до 5 дней
                val limitedGroupedData = groupedData.entries.take(5)

                val dataPointsMax = mutableListOf<Pair<String, Double>>()
                val dataPointsMin = mutableListOf<Pair<String, Double>>()

                limitedGroupedData.forEach { (date, forecasts) ->
                    val maxTemp = forecasts.maxOf { it.main.temp.toDouble() }
                    val minTemp = forecasts.minOf { it.main.temp.toDouble() }
                    val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault()).format(
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)!!
                    )
                    dataPointsMax.add(Pair(dayOfWeek, maxTemp))
                    dataPointsMin.add(Pair(dayOfWeek, minTemp))
                }

                updateChart(dataPointsMax, dataPointsMin)
            } else {
                Log.e("ExtendedForecast", "Failed to fetch forecast data")
            }
        }
    }

    private fun updateChart(dataPointsMax: List<Pair<String, Double>>, dataPointsMin: List<Pair<String, Double>>) {
        // Точки для максимальной температуры
        val entriesMax = dataPointsMax.mapIndexed { index, data ->
            Entry(index.toFloat(), data.second.toFloat())
        }
        val dataSetMax = LineDataSet(entriesMax, "Max Temperature").apply {
            color = ContextCompat.getColor(this@ExtendedForecastActivity, R.color.primaryColor)
            valueTextColor = ContextCompat.getColor(this@ExtendedForecastActivity, R.color.black)
            lineWidth = 2f
            circleRadius = 5f
            setDrawValues(true)
            valueTextSize = 14f
            valueFormatter = ValueFormatterWithUnit("°C") // Добавление °C
        }

        // Точки для минимальной температуры
        val entriesMin = dataPointsMin.mapIndexed { index, data ->
            Entry(index.toFloat(), data.second.toFloat())
        }
        val dataSetMin = LineDataSet(entriesMin, "Min Temperature").apply {
            color = Color.BLUE
            valueTextColor = ContextCompat.getColor(this@ExtendedForecastActivity, R.color.black)
            lineWidth = 2f
            circleRadius = 5f
            setDrawValues(true)
            valueTextSize = 14f
            valueFormatter = ValueFormatterWithUnit("°C") // Добавление °C
        }

        val lineData = LineData(dataSetMax, dataSetMin)

        val lineChart = findViewById<LineChart>(R.id.forecastChart)

        // Настройка оси X для отображения дней недели
        val daysOfWeek = dataPointsMax.map { it.first }

        lineChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(daysOfWeek)
            granularity = 1f
            position = XAxis.XAxisPosition.BOTTOM
            textColor = ContextCompat.getColor(this@ExtendedForecastActivity, R.color.black)
            setDrawGridLines(true)
            gridColor = ContextCompat.getColor(this@ExtendedForecastActivity, R.color.lightGray)
            axisMinimum = -0.5f // Смещение влево для центрирования
            axisMaximum = daysOfWeek.size - 0.5f // Смещение вправо для центрирования
        }

        // Применение данных к графику
        lineChart.apply {
            data = lineData
            description.isEnabled = false
            legend.isEnabled = false
            xAxis.setDrawGridLines(true)
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            setDrawMarkers(true)
            invalidate() // Перерисовка графика
        }
    }


    private fun initializeButtons() {
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish() // Закрытие текущей активности
        }
    }
}

class ValueFormatterWithUnit(private val unit: String) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "$value$unit"
    }
}

