package com.example.lab4.ui.detail

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
class DetailViewModel @Inject constructor(private val appDatabaseManager: AppDatabaseManager) :
    BaseViewModel() {

    private val userMutableStateFlow = MutableStateFlow<UserBD?>(null)
    val userStateFlow: StateFlow<UserBD?> = userMutableStateFlow

    fun getUserById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = appDatabaseManager.db.userDao().getUserById(id)
            userMutableStateFlow.value = user
        }
    }
}