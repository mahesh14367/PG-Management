package com.appsv.loginapp.login.domain.model

import com.appsv.loginapp.login.domain.utils.formatDateTime
import java.util.UUID

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val amount: Double=1.00,
    val status: String, // "SUCCESS", "FAILED", "PENDING"
    val timestamp: String = formatDateTime(System.currentTimeMillis()),
    val phonepeRefId: String? = null
)
