package com.appsv.loginapp.login.presentation.PaymentIntegration.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appsv.loginapp.login.domain.model.Transaction

@Composable
fun PaymentSuccessUI(transaction: Transaction, onBack: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            modifier = Modifier.size(72.dp).align(Alignment.CenterHorizontally),
            tint = Color.Green
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Payment Successful!", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Amount: ₹${transaction.amount.toInt()}", textAlign = TextAlign.Center)
        Text("Transaction ID: ${transaction.id.take(8)}", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text("Back to Home", textAlign = TextAlign.Center)
        }
    }
}