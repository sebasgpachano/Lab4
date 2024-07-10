package com.example.lab4.ui.list

import androidx.lifecycle.viewModelScope
import com.example.lab4.data.model.user.UserModel
import com.example.lab4.data.repository.BaseResponse
import com.example.lab4.data.repository.bbdd.user.UserBD
import com.example.lab4.data.usecase.DeleteUserUseCase
import com.example.lab4.data.usecase.GetAllUsersUseCase
import com.example.lab4.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) :
    BaseViewModel() {

    private val listUsersMutableStateFlow = MutableStateFlow<List<UserModel>>(emptyList())
    val listUsersStateFlow: StateFlow<List<UserModel>> = listUsersMutableStateFlow

    private val successSharedFlow = MutableSharedFlow<Boolean>()
    val successFlow: SharedFlow<Boolean> = successSharedFlow

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllUsersUseCase().collect { user ->
                when (user) {
                    is BaseResponse.Error -> {
                        loadingMutableSharedFlow.emit(false)
                        errorMutableSharedFlow.emit(user.error)
                    }

                    is BaseResponse.Success -> {
                        loadingMutableSharedFlow.emit(false)
                        listUsersMutableStateFlow.value = user.data
                    }
                }
            }
        }
    }

    fun deleteUser(user: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUserUseCase(user).collect {
                when (it) {
                    is BaseResponse.Error -> {
                        errorMutableSharedFlow.emit(it.error)
                    }

                    is BaseResponse.Success -> {
                        getUsers()
                        successSharedFlow.emit(true)
                    }
                }
            }
        }
    }
}