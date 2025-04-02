package com.appsv.loginapp.login.presentation.admin_home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.loginapp.login.data.repository.DataBaseRepositoryImpl
import com.appsv.loginapp.login.domain.model.Users
import com.appsv.loginapp.login.domain.repository.DataBaseRepository
import com.appsv.loginapp.login.domain.utils.Role
import com.appsv.loginapp.login.presentation.auth_state.AuthState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminScreenViewModel : ViewModel() {
    private val repository: DataBaseRepository = DataBaseRepositoryImpl()

    private val _state = MutableStateFlow(AdminScreenState())
    val state = _state.asStateFlow()


    init {
       getAllUsers()
    }


    suspend  fun onEvent(events: AdminScreenEvent) {
        when (events) {

            is AdminScreenEvent.UpdateUser -> {
                updateUser(events.user)
            }

            is AdminScreenEvent.DeleteUser -> {
                deleteUser(events.userId)
            }

            is AdminScreenEvent.Refresh -> {
                _state.value= _state.value.copy(
                    isLoading = true
                )
                delay(2000)
                getAllUsers()
            }
        }
    }



    private fun deleteUser(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )

            delay(2000)
            repository.deleteUser(userId = id)

            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }

    private fun updateUser(user: Users) {
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }


    private fun getAllUsers() {
        viewModelScope.launch {
            repository.getAllUsers().collect { usersList ->
                Log.d("usersList", usersList.toString())
                _state.value = _state.value.copy(
                    usersList = usersList?: emptyList(),
                    isLoading = false
                )
            }
        }
    }


}