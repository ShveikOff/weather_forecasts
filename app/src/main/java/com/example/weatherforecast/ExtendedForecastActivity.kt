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

        // Настраиваем Spinner для выбора опций прогноза
        val spinner: Spinner = findViewById(R.id.forecastSpinner)
        setupSpinner(spinner)

        // Настраиваем график с данными
        val chart: LineChart = findViewById(R.id.forecastChart)
        setupPlaceholderChartData(chart)
    }

    /**
     * Настраивает Spinner с опциями для прогноза
     */
    private fun setupSpinner(spinner: Spinner) {
        ArrayAdapter.createFromResource(
            this,
            R.array.forecast_options, // Массив строк в ресурсах
            R.layout.spinner_item // Кастомный layout для выбранного элемента
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.dropdown_item) // Кастомный layout для выпадающего списка
            spinner.adapter = adapter
        }
    }

    /**
     * Создает и отображает график с тестовыми данными
     */
    private fun setupPlaceholderChartData(chart: LineChart) {
        // Список точек данных для графика
        val entries = listOf(
            Entry(0f, 10f),
            Entry(1f, 15f),
            Entry(2f, 8f),
            Entry(3f, 12f),
            Entry(4f, 18f),
            Entry(5f, 20f)
        )

        // Создаем DataSet для графика
        val dataSet = LineDataSet(entries, "Temperature over Time").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            lineWidth = 2f
            circleRadius = 4f
            setCircleColor(Color.BLUE)
        }

        // Создаем объект LineData и добавляем его на график
        val lineData = LineData(dataSet)
        chart.apply {
            data = lineData
            invalidate() // Перерисовываем график
        }
    }
}
