package com.example.weatherforecast.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val preferences: String, // Настройки и предпочтения пользователя
    val answers: String      // Ответы на вопросы UserQuizScreen в виде JSON или строки
)
