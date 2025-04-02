package com.appsv.loginapp.login.domain.utils

fun getFirstName(name:String?):String{
    val nameRegex = Regex("""^([^\s]+)""")
    return name?.let { nameRegex.find(it)?.value } ?:"Guest"
}