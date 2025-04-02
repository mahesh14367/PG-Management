package com.appsv.loginapp.login.domain.utils

import java.util.Calendar

// Time-based greeting generator
fun getTimeBasedGreeting(): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    return when (hour) {
        in 5..11 -> "Good Morning!"
        in 12..16 -> "Good Afternoon!"
        in 17..22 -> "Good Evening!"
        else -> "Hello!"
    }
}