package com.appsv.loginapp.login.presentation.signup_screen.upload_state

sealed class ImageUploadState {
    object Idle : ImageUploadState()
    object Loading : ImageUploadState()
    data class Success(val imageUrl: String) : ImageUploadState()
    data class Error(val message: String) : ImageUploadState()
}