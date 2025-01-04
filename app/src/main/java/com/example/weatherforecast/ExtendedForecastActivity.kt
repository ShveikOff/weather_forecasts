package com.example.weatherforecast

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
        val dataSetMax = LineDataSet(entriesMax, "Max Temperature")
        dataSetMax.color = ContextCompat.getColor(this, R.color.primaryColor)
        dataSetMax.valueTextColor = ContextCompat.getColor(this, R.color.black)
        dataSetMax.lineWidth = 2f
        dataSetMax.circleRadius = 4f
        dataSetMax.setDrawValues(true)
        dataSetMax.valueTextSize = 14f

        // Точки для минимальной температуры
        val entriesMin = dataPointsMin.mapIndexed { index, data ->
            Entry(index.toFloat(), data.second.toFloat())
        }
        val dataSetMin = LineDataSet(entriesMin, "Min Temperature")
        dataSetMin.color = Color.BLUE
        dataSetMin.valueTextColor = ContextCompat.getColor(this, R.color.black)
        dataSetMin.lineWidth = 2f
        dataSetMin.circleRadius = 4f
        dataSetMin.setDrawValues(true)
        dataSetMin.valueTextSize = 14f

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
}
