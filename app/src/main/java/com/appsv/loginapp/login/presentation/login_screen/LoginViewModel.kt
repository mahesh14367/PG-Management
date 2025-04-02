package com.appsv.loginapp.login.presentation.login_screen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.loginapp.login.data.repository.AuthenticationRepositoryImpl
import com.appsv.loginapp.login.domain.model.LoginCred
import com.appsv.loginapp.login.domain.model.SignUpCred
import com.appsv.loginapp.login.domain.model.Users
import com.appsv.loginapp.login.domain.repository.AuthenticationRepository
import com.appsv.loginapp.login.domain.utils.CloudinaryHelper
import com.appsv.loginapp.login.presentation.signup_screen.SignUpState
import com.appsv.loginapp.login.presentation.signup_screen.upload_state.FileUploadState
import com.appsv.loginapp.login.presentation.signup_screen.upload_state.ImageUploadState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository: AuthenticationRepository = AuthenticationRepositoryImpl()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState

    private val _Image_uploadState = MutableStateFlow<ImageUploadState>(ImageUploadState.Idle)
    val imageUploadState: StateFlow<ImageUploadState> = _Image_uploadState

    private val _File_uploadState = MutableStateFlow<FileUploadState>(FileUploadState.Idle)
    val fileUploadState: StateFlow<FileUploadState> = _File_uploadState


    fun onEvent(events: Event) {
        when (events) {
            is Event.LoginCheck -> {
                authenticate(events.loginCheck)
            }

            is Event.SaveSignUpCred -> {
                saveSignUpCred(events.saveSignUpCred, events.user)
            }

            is Event.LoginResetState -> {
                resetLoginState()
            }

            is Event.SignUpResetState -> {
                resetSignUpState()
            }
        }
    }

    fun uploadImageToCloudinary(context: Context, uri: Uri) {
        viewModelScope.launch {
            _Image_uploadState.value = ImageUploadState.Loading
            try {
                val imageUrl = CloudinaryHelper.uploadImage(context, uri)
                _Image_uploadState.value = ImageUploadState.Success(imageUrl)
            } catch (e: Exception) {
                _Image_uploadState.value = ImageUploadState.Error(e.message ?: "Upload failed")
            }
        }
    }

    fun uploadFileToCloudinary(context: Context, uri: Uri) {
        viewModelScope.launch {
            _File_uploadState.value = FileUploadState.Loading
            try {
                val fileUrl = CloudinaryHelper.uploadImage(context, uri)
                _File_uploadState.value = FileUploadState.Success(fileUrl)
            } catch (e: Exception) {
                _File_uploadState.value = FileUploadState.Error(e.message ?: "Upload failed")
            }
        }
    }

    fun resetBothImageandFileUploadState(){
        _Image_uploadState.value=ImageUploadState.Idle
        _File_uploadState.value=FileUploadState.Idle
    }
    private fun authenticate(loginCred: LoginCred) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading
                val result = repository.authenticate(loginCred)
                if (result.isSuccess) {
                    delay(300)
                    _loginState.value = LoginState.Success(user = result.getOrNull()!!)
                } else {
                    _loginState.value = LoginState.Error(
                        result.exceptionOrNull()?.message ?: "Login failed"
                    )
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Authentication error: ${e.message}")
            }
        }
    }

    private fun saveSignUpCred(signUpCred: SignUpCred, user: Users) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            val result = repository.saveSignUpCred(signUpCred, user)
            _signUpState.value = if (result.isSuccess) {
                val newUser = result.getOrNull()!!
                SignUpState.Success(newUser)
            } else {
                SignUpState.Error(result.exceptionOrNull()?.message ?: "SignUp failed")
            }
        }
    }

    private fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    private fun resetSignUpState() {
        _signUpState.value = SignUpState.Idle
    }


}

