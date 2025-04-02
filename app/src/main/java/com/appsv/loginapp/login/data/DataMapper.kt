package com.appsv.loginapp.login.data

import com.appsv.loginapp.R
import com.appsv.loginapp.core.data.GuestDTO
import com.appsv.loginapp.core.data.StaffDTO
import com.appsv.loginapp.core.data.UsersDTO
import com.appsv.loginapp.login.domain.model.Guest
import com.appsv.loginapp.login.domain.model.Staff
import com.appsv.loginapp.login.domain.model.Users
import java.util.UUID

fun Guest.toGuestDTO(): GuestDTO {
    return GuestDTO(
        id = id?:UUID.randomUUID().toString(),
        name=name,
        roomNumber = roomNumber,
        paid = paid,
        profilePic = profilePic?:R.drawable.ic_launcher_foreground
    )
}

fun Staff.toStaffDTO():StaffDTO{
    return StaffDTO(
        id=id?:UUID.randomUUID().toString(),
        name = name,
        position = position,
        shift = shift
    )
}
