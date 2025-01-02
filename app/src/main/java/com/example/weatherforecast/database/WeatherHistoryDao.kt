package com.example.weatherforecast.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherHistoryDao {

    // Тут не работает ряд методов, они вызывают ошибку к которой я не нашел решения

    // @Insert(onConflict = OnConflictStrategy.REPLACE) Эта строка вызывает ошибку!!!
    // suspend fun insertWeatherHistory(weatherHistory: WeatherHistory): Long

    @Query("SELECT * FROM weather_history WHERE city = :city ORDER BY timestamp DESC LIMIT 1")
    fun getLatestWeatherForCity(city: String): Flow<WeatherHistory?>

    // @Query("DELETE FROM weather_history WHERE timestamp < :timeLimit") Эта строка вызывает ошибку!!!
    // suspend fun deleteOldWeatherData(timeLimit: Long): Int
}
