package com.example.lab4.ui.form

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.lab4.data.repository.bbdd.AppDatabaseManager
import com.example.lab4.data.repository.bbdd.user.UserBD
import com.example.lab4.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(private val appDatabaseManager: AppDatabaseManager) :
    BaseViewModel() {

    private val userMutableStateFlow = MutableStateFlow<UserBD?>(null)
    val userStateFlow: StateFlow<UserBD?> = userMutableStateFlow

    init {
        printLogsUsers()
    }

    private fun printLogsUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabaseManager.db.userDao().getAllUsers().collect { users ->
                Log.d(TAG, "l> database, Users:")
                users.forEach { user ->
                    Log.d(TAG, "$user")
                }
                Log.d(TAG, "End\n\n\n")
            }
        }
    }

    fun addUser(user: UserBD) {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabaseManager.db
                .userDao()
                .insertUser(user)
        }
    }

    fun getUserById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = appDatabaseManager.db.userDao().getUserById(id)
            userMutableStateFlow.value = user
        }
    }

    //TODO change parameters
    fun updateUser(
        id: Int,
        name: String,
        color: String,
        birthDate: String,
        city: String,
        number: Int,
        lat: Double,
        lon: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabaseManager.db.userDao()
                .updateUser(id, name, color, birthDate, city, number, lat, lon)
        }
    }
}