package com.example.lab4.ui.list

import androidx.lifecycle.viewModelScope
import com.example.lab4.data.repository.bbdd.AppDatabaseManager
import com.example.lab4.data.repository.bbdd.user.UserBD
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
class ListViewModel @Inject constructor(private val appDatabaseManager: AppDatabaseManager) :
    BaseViewModel() {

    private val listUsersMutableStateFlow = MutableStateFlow<List<UserBD>>(emptyList())
    val listUsersStateFlow: StateFlow<List<UserBD>> = listUsersMutableStateFlow

    private val successSharedFlow = MutableSharedFlow<Boolean>()
    val successFlow: SharedFlow<Boolean> = successSharedFlow

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabaseManager.db.userDao().getAllUsers().collect { users ->
                listUsersMutableStateFlow.value = users
            }
        }
    }

    fun deleteUser(user: UserBD) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                appDatabaseManager.db.userDao().deleteUser(user)
                getUsers()
                successSharedFlow.emit(true)
            } catch (e: Exception) {
                //error
            }
        }
    }
}