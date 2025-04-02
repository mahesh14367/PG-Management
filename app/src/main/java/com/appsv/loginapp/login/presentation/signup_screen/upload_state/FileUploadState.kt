package com.appsv.loginapp.login.presentation.signup_screen.upload_state

sealed class FileUploadState {
    object Idle : FileUploadState()
    object Loading : FileUploadState()
    data class Success(val fileUrl: String) : FileUploadState()
    data class Error(val message: String) : FileUploadState()
}