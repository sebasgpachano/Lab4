package com.example.lab4.data.repository.bbdd

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDatabaseManager @Inject constructor(@ApplicationContext context: Context) {
    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "lab4-database"
    ).build()
}