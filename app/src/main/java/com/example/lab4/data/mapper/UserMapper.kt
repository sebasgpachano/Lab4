package com.example.lab4.data.mapper

import com.example.lab4.data.model.user.UserModel
import com.example.lab4.data.repository.bbdd.user.UserBD
import javax.inject.Inject

class UserMapper @Inject constructor() {
    fun toModel(entity: UserBD): UserModel {
        return UserModel(
            id = entity.id,
            name = entity.name,
            favoriteColor = entity.favoriteColor,
            birthDate = entity.birthDate,
            favoriteCity = entity.favoriteCity,
            favoriteNumber = entity.favoriteNumber,
            latitude = entity.latitude,
            longitude = entity.longitude
        )
    }

    fun toEntity(model: UserModel): UserBD {
        return UserBD(
            id = model.id,
            name = model.name,
            favoriteColor = model.favoriteColor,
            birthDate = model.birthDate,
            favoriteCity = model.favoriteCity,
            favoriteNumber = model.favoriteNumber,
            latitude = model.latitude,
            longitude = model.longitude
        )
    }
}