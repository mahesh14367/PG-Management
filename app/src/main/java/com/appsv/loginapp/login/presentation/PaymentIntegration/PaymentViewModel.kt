package com.appsv.loginapp.login.presentation.PaymentIntegration

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.loginapp.login.data.repository.PaymentRepositoryImpl
import com.appsv.loginapp.login.domain.model.Transaction
import com.appsv.loginapp.login.domain.repository.PaymentRepository
import com.appsv.loginapp.login.domain.utils.formatDateTime
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentViewModel : ViewModel() {

    private val paymentRepo: PaymentRepository = PaymentRepositoryImpl()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState = _paymentState.asStateFlow()

    private var pendingTransaction: Transaction? = null

    fun initiatePayment(amount: Double, context: Context, launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading

            // Create pending transaction
            val transaction = Transaction(
                userId = auth.currentUser?.uid ?: "",
                amount = amount,
                status = "PENDING"
            )

            // Save pending transaction first
            paymentRepo.saveTransaction(transaction).onSuccess {
                pendingTransaction = transaction
                launchPayment(transaction, amount, context, launcher)
            }.onFailure {
                _paymentState.value = PaymentState.Error("Failed to initialize payment: ${it.message}")
            }
        }
    }

    private suspend fun launchPayment(
        transaction: Transaction,
        amount: Double,
        context: Context,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) {
        try {
            // PhonePe deep link
            val upiId = "9032610241@ptsbi"
            val transactionId = transaction.id
            val orderId = transactionId
            val paymentNote = "Hostel Fees"

            val phonePeUri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId) // UPI ID
                .appendQueryParameter("pn", "Receiver Name") // Name
                .appendQueryParameter("tr", transactionId) // Transaction ID
                .appendQueryParameter("tn", paymentNote) // Transaction note
                .appendQueryParameter("am", amount.toString()) // Amount
                .appendQueryParameter("cu", "INR") // Currency
                .build()

            val intent = Intent(Intent.ACTION_VIEW, phonePeUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            intent.setPackage("com.phonepe.app")

            // Check if PhonePe is installed
            if (intent.resolveActivity(context.packageManager) != null) {
                launcher.launch(intent)
            } else {
                // Fallback to other UPI apps or web
                val fallbackIntent = Intent(Intent.ACTION_VIEW, phonePeUri)
                if (fallbackIntent.resolveActivity(context.packageManager) != null) {
                    launcher.launch(fallbackIntent)
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "No UPI app found to handle payment",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    _paymentState.value = PaymentState.Error("No UPI app installed")
                }
            }
        } catch (e: Exception) {
            _paymentState.value = PaymentState.Error("Payment failed: ${e.message}")
        }
    }

    fun handlePaymentResult(result: ActivityResult) {
        pendingTransaction?.let { transaction ->
            viewModelScope.launch {
                try {
                    // In a real app, you would parse the actual result data
                    // For demo purposes, we'll assume success if we get any result
                    val isSuccess = result.resultCode == android.app.Activity.RESULT_OK

                    val updatedTransaction = transaction.copy(
                        status = if (isSuccess) "SUCCESS" else "FAILED",
                        phonepeRefId = "PHONEPE_${formatDateTime(System.currentTimeMillis())}"
                    )

                    paymentRepo.saveTransaction(updatedTransaction).onSuccess {
                        _paymentState.value = if (isSuccess) {
                            PaymentState.Success(updatedTransaction)
                        } else {
                            PaymentState.Error("Payment was cancelled or failed")
                        }
                    }.onFailure {
                        _paymentState.value = PaymentState.Error("Payment completed but failed to update record")
                    }
                } catch (e: Exception) {
                    _paymentState.value = PaymentState.Error("Error processing payment result: ${e.message}")
                }
            }
        }
    }
}