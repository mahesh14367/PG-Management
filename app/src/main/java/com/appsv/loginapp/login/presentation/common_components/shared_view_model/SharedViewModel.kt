package com.appsv.loginapp.login.presentation.common_components.shared_view_model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.appsv.loginapp.login.domain.model.Users
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {
    private val _user = MutableStateFlow<Users?>(null)
    val user: StateFlow<Users?> = _user

    fun setUser(user: Users) {
        _user.value = user
    }
}