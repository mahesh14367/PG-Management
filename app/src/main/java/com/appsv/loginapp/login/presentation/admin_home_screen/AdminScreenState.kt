package com.appsv.loginapp.login.presentation.admin_home_screen

import com.appsv.loginapp.login.domain.model.Users

data class AdminScreenState(
    val isLoading:Boolean=true,
    val usersList:List<Users> = emptyList(),
)