package com.example.weatherforecast.database

import androidx.room.*

@Dao
interface UserDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(userData: UserData)

    @Update
    suspend fun updateUserData(userData: UserData)

    @Delete
    suspend fun deleteUserData(userData: UserData)

    @Query("SELECT * FROM user_data WHERE id = :id LIMIT 1")
    suspend fun getUserDataById(id: Int): UserData?

    @Query("SELECT * FROM user_data")
    suspend fun getAllUserData(): List<UserData>
}
