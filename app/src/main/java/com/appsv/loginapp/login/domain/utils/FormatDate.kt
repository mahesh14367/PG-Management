package com.appsv.loginapp.login.domain.utils

import java.text.SimpleDateFormat

import java.util.*

fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.ENGLISH)
    sdf.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
    return sdf.format(Date(timestamp))
}
