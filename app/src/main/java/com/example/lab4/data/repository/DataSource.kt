package com.example.lab4.data.repository

import com.example.lab4.data.model.user.UserModel
import com.example.lab4.data.repository.bbdd.user.UserBD
import kotlinx.coroutines.flow.Flow

interface DataSource {
    fun insertUser(user: UserModel): Flow<BaseResponse<Boolean>>
    fun getAllUsers(): Flow<BaseResponse<List<UserModel>>>
    fun getUserById(id: Int): Flow<BaseResponse<UserModel?>>
    fun deleteUser(user: UserModel): Flow<BaseResponse<Boolean>>
    fun updateUser(user: UserModel): Flow<BaseResponse<Boolean>>
}