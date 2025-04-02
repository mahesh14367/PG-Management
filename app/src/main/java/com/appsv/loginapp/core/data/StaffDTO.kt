package com.appsv.loginapp.core.data

import java.util.UUID

data class StaffDTO(
    val id:String?= null,
    val name: String="",
    val position: String="",
    val shift: String=""
)
