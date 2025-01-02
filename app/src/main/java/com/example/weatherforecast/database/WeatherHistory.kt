package com.example.weatherforecast.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_history")
data class WeatherHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val city: String,
    val weatherData: String,  // JSON строка с прогнозами
    val timestamp: Long       // Время добавления для устаревших данных
)
