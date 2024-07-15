package com.example.lab4.data.repository.bbdd

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab4.data.repository.bbdd.user.UserBD
import com.example.lab4.data.repository.bbdd.user.UserDAO

@Database(entities = [UserBD::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
}