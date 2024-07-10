package com.example.lab4.data.repository

import com.example.lab4.data.model.user.UserModel
import com.example.lab4.data.repository.bbdd.LocalDataSource
import com.example.lab4.data.repository.bbdd.user.UserBD
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataProvider @Inject constructor(private val localDataSource: LocalDataSource) : DataSource {
    override fun insertUser(user: UserModel): Flow<BaseResponse<Boolean>> {
        return localDataSource.insertUser(user)
    }

    override fun getAllUsers(): Flow<BaseResponse<List<UserModel>>> {
        return localDataSource.getAllUsers()
    }

    override fun getUserById(id: Int): Flow<BaseResponse<UserModel?>> {
        return localDataSource.getUserById(id)
    }

    override fun deleteUser(user: UserModel): Flow<BaseResponse<Boolean>> {
        return localDataSource.deleteUser(user)
    }

    override fun updateUser(user: UserModel): Flow<BaseResponse<Boolean>> {
        return localDataSource.updateUser(user)
    }
}