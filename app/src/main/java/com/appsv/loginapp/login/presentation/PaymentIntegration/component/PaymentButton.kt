package com.appsv.loginapp.login.presentation.PaymentIntegration.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun PaymentButton(amount: Double, onClick: () -> Unit) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Pay with PhonePe",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center

            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Amount: ₹${amount.toInt()}",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(72.dp).align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    painter = painterResource(R.drawable.phone_pe_icon), // Add PhonePe icon
                    contentDescription = "PhonePe",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.Unspecified
                )
            }
            Text("Tap to pay with PhonePe", textAlign = TextAlign.Center)
        }

}