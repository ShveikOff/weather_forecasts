package com.example.weatherforecast.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.example.weatherforecast.database.UserData

@Dao
interface UserDataDao {

    // Тут не работает ряд методов, они вызывают ошибку к которой я не нашел решения

    // @Insert(onConflict = OnConflictStrategy.REPLACE) Эта строка вызывает ошибку!!!
    // suspend fun insertUserData(userData: UserData): Long

    // @Update Эта строка вызывает ошибку!!!
    // suspend fun updateUserData(userData: UserData)

    // @Delete Эта строка вызывает ошибку !!!
    // suspend fun deleteUserData(userData: UserData): Int

    @Query("SELECT * FROM user_data WHERE id = :id LIMIT 1")
    fun getUserDataById(id: Int): Flow<UserData?>

    @Query("SELECT * FROM user_data")
    fun getAllUserData(): Flow<List<UserData>>
}
