package com.appsv.loginapp.login.presentation.PaymentIntegration

import com.appsv.loginapp.login.domain.model.Transaction

sealed class PaymentState {
    object Idle : PaymentState()
    object Loading : PaymentState()
    data class Success(val transaction: Transaction) : PaymentState()
    data class Error(val message: String) : PaymentState()
}