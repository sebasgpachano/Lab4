package com.example.lab4.data.repository.bbdd

import androidx.room.Room
import com.example.lab4.ui.base.BaseProjectApplication
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
abstract class AppDatabaseManager @Inject constructor(baseProjectApplication: BaseProjectApplication) {
    val db = Room.databaseBuilder(
        baseProjectApplication.applicationContext,
        AppDatabase::class.java,
        "lab4-database"
    ).build()
}