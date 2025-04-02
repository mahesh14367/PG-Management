package com.appsv.loginapp.login.domain.repository

import com.appsv.loginapp.login.domain.model.LoginCred
import com.appsv.loginapp.login.domain.model.SignUpCred
import com.appsv.loginapp.login.domain.model.Users
import com.google.firebase.auth.FirebaseUser

interface AuthenticationRepository {

    suspend fun saveSignUpCred(signUpCred: SignUpCred,user:Users):Result<FirebaseUser>

    suspend fun authenticate(loginCred: LoginCred):Result<Users?>

    suspend fun getUserData(userId: String): Users?

}