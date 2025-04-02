package com.appsv.loginapp.core.domain_mapper

import com.appsv.loginapp.core.data.GuestDTO
import com.appsv.loginapp.core.data.StaffDTO
import com.appsv.loginapp.core.data.UsersDTO
import com.appsv.loginapp.login.domain.model.Guest
import com.appsv.loginapp.login.domain.model.Staff
import com.appsv.loginapp.login.domain.model.Users
import com.appsv.loginapp.login.domain.utils.Gender
import com.appsv.loginapp.login.domain.utils.Occupation
import com.appsv.loginapp.login.domain.utils.Role
import java.util.UUID

fun GuestDTO.toGuest(): Guest {

    return Guest(
        id = id!!,
        name=name,
        roomNumber = roomNumber,
        paid = paid,
        profilePic = profilePic!!
    )
}

fun StaffDTO.toStaff(): Staff {
    return Staff(
        id=id!!,
        name =name,
        position = position,
        shift = shift
    )
}

fun UsersDTO.toUsers(): Users {
    return Users(
        userId=userId,
        profilePic=profilePic,
        name=name,
        dob=dob,
        emailId=emailId,
        mobileNumber=mobileNumber,
        password=password,
        role=role,
        idProofDoc=idProofDoc,
        facility=facility,
        roomNo=roomNo,
        isPaid=isPaid,
        gender=gender,
        occupation=occupation
    )
}

