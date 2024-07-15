package com.example.lab4.data.model.user

import com.example.lab4.data.model.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: Int = 0,
    val name: String,
    val favoriteColor: String,
    val birthDate: String,
    val favoriteCity: String,
    val favoriteNumber: Int,
    val latitude: Double,
    val longitude: Double
) : BaseModel()