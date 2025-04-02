package com.appsv.loginapp.login.domain.model

import java.util.UUID

data class Staff(
    val id:String?= null,
    val name: String,
    val position: String,
    val shift: String
)