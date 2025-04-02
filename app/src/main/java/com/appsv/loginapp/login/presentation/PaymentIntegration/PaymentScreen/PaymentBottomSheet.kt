package com.appsv.loginapp.login.presentation.PaymentIntegration.PaymentScreen

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.appsv.loginapp.login.presentation.PaymentIntegration.PaymentState
import com.appsv.loginapp.login.presentation.PaymentIntegration.PaymentViewModel
import com.appsv.loginapp.login.presentation.PaymentIntegration.component.PaymentButton
import com.appsv.loginapp.login.presentation.PaymentIntegration.component.PaymentErrorUI
import com.appsv.loginapp.login.presentation.PaymentIntegration.component.PaymentSuccessUI

@Composable
fun PaymentBottomSheet(
    paymentViewModel: PaymentViewModel,
    context: Context,
    onClickBack: () -> Unit
) {
    val paymentState by paymentViewModel.paymentState.collectAsState()
    val currentContext = LocalContext.current

    val paymentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            paymentViewModel.handlePaymentResult(result)
        }
    )

    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val state = paymentState) {
            is PaymentState.Idle -> {
                PaymentButton(
                    amount = 100.0, // Fixed amount
                    onClick = {
                        paymentViewModel.initiatePayment(
                            amount = 100.0,
                            context = currentContext,
                            launcher = paymentLauncher
                        )
                    }
                )
            }
            is PaymentState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Processing payment...")
            }
            is PaymentState.Success -> {
                PaymentSuccessUI(
                    transaction = state.transaction,
                    onBack = onClickBack
                )
            }
            is PaymentState.Error -> {
                PaymentErrorUI(
                    message = state.message,
                    onRetry = {
                        paymentViewModel.initiatePayment(
                            amount = 100.0,
                            context = currentContext,
                            launcher = paymentLauncher
                        )
                    },
                    onBack = onClickBack
                )
            }
        }
    }
}