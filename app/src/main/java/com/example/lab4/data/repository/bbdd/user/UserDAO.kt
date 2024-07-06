package com.example.lab4.data.repository.bbdd.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userBD: UserBD)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserBD>>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Int): UserBD?

    @Delete
    fun deleteUser(user: UserBD)

    @Query("DELETE FROM users WHERE id = :id")
    fun deleteUserById(id: Int)
}