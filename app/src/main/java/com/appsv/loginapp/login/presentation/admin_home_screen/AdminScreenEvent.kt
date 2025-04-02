package com.appsv.loginapp.login.presentation.admin_home_screen

import com.appsv.loginapp.login.domain.model.Users

sealed class AdminScreenEvent {

    data class UpdateUser(val user: Users) : AdminScreenEvent()

    data class DeleteUser(val userId: String) : AdminScreenEvent()

    data object Refresh:AdminScreenEvent()

}