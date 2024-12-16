package com.example.weatherforecast.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserData::class, WeatherHistory::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDataDao(): UserDataDao
    abstract fun weatherHistoryDao(): WeatherHistoryDao
}
