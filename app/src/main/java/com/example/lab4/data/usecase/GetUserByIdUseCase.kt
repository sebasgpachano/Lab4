package com.example.lab4.data.usecase

import com.example.lab4.data.model.user.UserModel
import com.example.lab4.data.repository.BaseResponse
import com.example.lab4.data.repository.DataProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(private val dataProvider: DataProvider) {
    operator fun invoke(id: Int): Flow<BaseResponse<UserModel?>> {
        return dataProvider.getUserById(id)
    }
}