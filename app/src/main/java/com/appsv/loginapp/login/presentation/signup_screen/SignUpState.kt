package com.appsv.loginapp.login.presentation.signup_screen

import com.google.firebase.auth.FirebaseUser

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    data class Success(val user:FirebaseUser) : SignUpState()
    data class Error(val message: String) : SignUpState()
}