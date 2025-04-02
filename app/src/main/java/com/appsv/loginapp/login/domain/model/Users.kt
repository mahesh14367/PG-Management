package com.appsv.loginapp.login.domain.model

import com.appsv.loginapp.login.domain.utils.Gender
import com.appsv.loginapp.login.domain.utils.Occupation
import com.appsv.loginapp.login.domain.utils.PaymentStatus
import com.appsv.loginapp.login.domain.utils.Role

data class Users(
    val userId:String?= null,
    val profilePic:String?=null,
    val name:String?=null,
    val dob:String?=null,
    val emailId:String?=null,
    val mobileNumber:String?=null,
    val password:String?=null,
    val role:String?= Role.USER.name,
    val idProofDoc:String?=null,
    val facility:String?=null,
    val roomNo:String?=null,
    val isPaid:String?=PaymentStatus.PAID.name,
    val gender:String?=Gender.MALE.name,
    val occupation:String?= Occupation.STUDENT.name
)
