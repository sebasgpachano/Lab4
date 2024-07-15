package com.example.lab4.data.repository.bbdd.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userBD: UserBD)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserBD>>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserBD?

    @Delete
    suspend fun deleteUser(user: UserBD)

    @Update
    suspend fun updateUser(user: UserBD)
}