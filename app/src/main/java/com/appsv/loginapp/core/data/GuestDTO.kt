package com.appsv.loginapp.core.data

data class GuestDTO(
    val id:String?=null,
    val name: String="",
    val roomNumber: String="",
    val paid: Boolean=false,
    val profilePic: Int?=null
)
