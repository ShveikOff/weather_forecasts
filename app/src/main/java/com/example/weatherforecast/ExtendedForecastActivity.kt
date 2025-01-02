package com.example.weatherforecast

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Spinner
import android.widget.ArrayAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ExtendedForecastActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extended_forecast)

        val spinner: Spinner = findViewById(R.id.forecastSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.forecast_options,
            R.layout.spinner_item // Кастомная разметка для выбранного элемента
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.dropdown_item) // Кастомная разметка для элементов списка
            spinner.adapter = adapter
        }

        val chart: LineChart = findViewById(R.id.forecastChart)
        setupPlaceholderChartData(chart)
    }

    private fun setupPlaceholderChartData(chart: LineChart) {
        // Создаем список захардкоженных данных
        val entries = listOf(
            Entry(0f, 10f),
            Entry(1f, 15f),
            Entry(2f, 8f),
            Entry(3f, 12f),
            Entry(4f, 18f),
            Entry(5f, 20f)
        )

        // Создаем набор данных с этими значениями
        val dataSet = LineDataSet(entries, "Temperature over Time")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setCircleColor(Color.BLUE)

        // Создаем LineData и передаем его графику
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate() // Обновляем график
    }
}
