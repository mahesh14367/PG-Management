package com.appsv.loginapp.login.presentation.PaymentIntegration.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appsv.loginapp.R

@Composable
fun PaymentErrorUI(message: String, onRetry: () -> Unit, onBack: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.error_icon), // Add PhonePe icon
            contentDescription = "Failed",
            modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Payment Failed", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text(message, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = onRetry) {
                Text("Retry Payment")
            }
            Button(onClick = onBack) {
                Text("Back to Home")
            }
        }
    }
}