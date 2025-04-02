package com.appsv.loginapp.login.presentation.auth_state

import com.appsv.loginapp.login.domain.model.Users

// Combined loading and user state
data class AuthState(
    val user: Users? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
