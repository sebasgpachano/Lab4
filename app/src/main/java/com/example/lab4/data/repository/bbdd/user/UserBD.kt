package com.example.lab4.data.repository.bbdd.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lab4.data.model.BaseModel

@Entity(tableName = "users")
data class UserBD(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val favoriteColor: String,
    val birthDate: String,
    val favoriteCity: String,
    val favoriteNumber: Int,
    val latitude: Double,
    val longitude: Double
) : BaseModel()