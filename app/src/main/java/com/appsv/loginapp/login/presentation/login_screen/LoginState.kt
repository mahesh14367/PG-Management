package com.appsv.loginapp.login.presentation.login_screen

import com.appsv.loginapp.login.domain.model.Users

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: Users?) : LoginState()
    data class Error(val message: String) : LoginState()
}