package com.example.weatherforecast.database

import androidx.room.*

@Dao
interface WeatherHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherHistory(weatherHistory: WeatherHistory)

    @Query("SELECT * FROM weather_history WHERE city = :city ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestWeatherForCity(city: String): WeatherHistory?

    @Query("DELETE FROM weather_history WHERE timestamp < :timeLimit")
    suspend fun deleteOldWeatherData(timeLimit: Long)
}
