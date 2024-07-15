package com.example.lab4.data.repository.bbdd

import android.content.Context
import com.example.lab4.R
import com.example.lab4.data.mapper.UserMapper
import com.example.lab4.data.model.ErrorModel
import com.example.lab4.data.model.user.UserModel
import com.example.lab4.data.repository.BaseResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val appDatabaseManager: AppDatabaseManager,
    @ApplicationContext private val context: Context,
    private val userMapper: UserMapper
) {
    fun insertUser(user: UserModel): Flow<BaseResponse<Boolean>> = flow {
        try {
            val userEntity = userMapper.toEntity(user)
            appDatabaseManager.db.userDao().insertUser(userEntity)
            emit(BaseResponse.Success(true))
        } catch (e: Exception) {
            val errorModel =
                ErrorModel("", "", e.message ?: context.getString(R.string.error_unknown_error))
            emit(BaseResponse.Error(errorModel))
        }
    }

    fun getAllUsers(): Flow<BaseResponse<List<UserModel>>> = flow {
        try {
            appDatabaseManager.db.userDao().getAllUsers().collect { usersEntities ->
                if (usersEntities.isNotEmpty()) {
                    val usersModels = usersEntities.map { userMapper.toModel(it) }
                    emit(BaseResponse.Success(usersModels))
                } else {
                    emit(BaseResponse.Error(ErrorModel("", "", "Empty list")))
                }
            }
        } catch (e: Exception) {
            val errorModel =
                ErrorModel("", "", e.message ?: context.getString(R.string.error_unknown_error))
            emit(BaseResponse.Error(errorModel))
        }
    }

    fun getUserById(id: Int): Flow<BaseResponse<UserModel?>> = flow {
        try {
            val userEntity = appDatabaseManager.db.userDao().getUserById(id)
            val userModel = userEntity?.let { userMapper.toModel(it) }
            emit(BaseResponse.Success(userModel))
        } catch (e: Exception) {
            val errorModel =
                ErrorModel("", "", e.message ?: context.getString(R.string.error_unknown_error))
            emit(BaseResponse.Error(errorModel))
        }
    }

    fun deleteUser(user: UserModel): Flow<BaseResponse<Boolean>> = flow {
        try {
            val userEntity = userMapper.toEntity(user)
            appDatabaseManager.db.userDao().deleteUser(userEntity)
            emit(BaseResponse.Success(true))
        } catch (e: Exception) {
            val errorModel =
                ErrorModel("", "", e.message ?: context.getString(R.string.error_unknown_error))
            emit(BaseResponse.Error(errorModel))
        }
    }

    fun updateUser(user: UserModel): Flow<BaseResponse<Boolean>> = flow {
        try {
            val userEntity = userMapper.toEntity(user)
            appDatabaseManager.db.userDao().updateUser(userEntity)
            emit(BaseResponse.Success(true))
        } catch (e: Exception) {
            val errorModel =
                ErrorModel("", "", e.message ?: context.getString(R.string.error_unknown_error))
            emit(BaseResponse.Error(errorModel))
        }
    }
}
