package com.example.weatherforecast.database

import android.content.Context
import androidx.room.Room

object DatabaseManager {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "weather_forecast_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
