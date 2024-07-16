package com.example.lab4.ui.form

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.lab4.data.model.user.UserModel
import com.example.lab4.data.repository.BaseResponse
import com.example.lab4.data.usecase.GetUserByIdUseCase
import com.example.lab4.data.usecase.InsertUserUseCase
import com.example.lab4.data.usecase.UpdateUserUseCase
import com.example.lab4.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val insertUserUseCase: InsertUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) :
    BaseViewModel() {

    private val userMutableStateFlow = MutableStateFlow<UserModel?>(null)
    val userStateFlow: StateFlow<UserModel?> = userMutableStateFlow

    fun addUser(user: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            insertUserUseCase(user).collect { result ->
                when (result) {
                    is BaseResponse.Error -> {
                        errorMutableSharedFlow.emit(result.error)
                    }

                    is BaseResponse.Success -> {
                        loadingMutableSharedFlow.emit(false)
                    }
                }
            }
        }
    }

    fun getUserById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingMutableSharedFlow.emit(true)
            getUserByIdUseCase(id).collect {
                when (it) {
                    is BaseResponse.Error -> {
                        loadingMutableSharedFlow.emit(false)
                        errorMutableSharedFlow.emit(it.error)
                    }

                    is BaseResponse.Success -> {
                        userMutableStateFlow.value = it.data
                        loadingMutableSharedFlow.emit(false)
                    }
                }
            }
        }
    }

    fun updateUser(user: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingMutableSharedFlow.emit(true)
            updateUserUseCase(user).collect {
                when (it) {
                    is BaseResponse.Error -> {
                        loadingMutableSharedFlow.emit(false)
                        errorMutableSharedFlow.emit(it.error)
                    }

                    is BaseResponse.Success -> {
                        loadingMutableSharedFlow.emit(false)
                    }
                }
            }
        }
    }
}