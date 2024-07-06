package com.example.lab4.data.repository.bbdd.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserBD(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val favoriteColor: String,
    val birthDate: String,
    val city: String,
    val number: Int,
    val lat: Double,
    val lon: Double
)