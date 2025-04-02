package com.appsv.loginapp.login.domain.repository

import com.appsv.loginapp.login.domain.model.Transaction

interface PaymentRepository {
    suspend fun saveTransaction(transaction: Transaction):Result<Unit>
}