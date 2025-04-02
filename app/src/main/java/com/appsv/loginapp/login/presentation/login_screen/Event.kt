package com.appsv.loginapp.login.presentation.login_screen

import com.appsv.loginapp.login.domain.model.LoginCred
import com.appsv.loginapp.login.domain.model.SignUpCred
import com.appsv.loginapp.login.domain.model.Users

sealed class Event {

    data class SaveSignUpCred(val saveSignUpCred:SignUpCred,val user: Users): Event()

    data class LoginCheck(val loginCheck:LoginCred): Event()

    object LoginResetState:Event()

    object SignUpResetState:Event()

}