package com.appsv.loginapp.login.domain.repository

import com.appsv.loginapp.login.domain.model.Guest
import com.appsv.loginapp.login.domain.model.Staff
import com.appsv.loginapp.login.domain.model.Users
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface DataBaseRepository {


    suspend fun getAllUsers():Flow<List<Users>?>

    suspend fun updateUser(user: Users)

    suspend fun deleteUser(userId:String)

}