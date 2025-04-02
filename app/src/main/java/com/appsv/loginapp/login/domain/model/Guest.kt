package com.appsv.loginapp.login.domain.model

import java.util.UUID

data class Guest(
    val id:String?= null,
    val name: String,
    val roomNumber: String,
    val paid: Boolean,
    val profilePic: Int
)
