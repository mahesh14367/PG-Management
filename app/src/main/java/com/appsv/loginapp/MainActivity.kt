package com.appsv.loginapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appsv.loginapp.core.ui.theme.LoginAppTheme
import com.appsv.loginapp.login.presentation.PaymentIntegration.PaymentViewModel
import com.appsv.loginapp.login.presentation.nav_graph.SetNavGraph
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    // Register for activity result to handle payment callback

//    private val paymentResultLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        val viewModel: PaymentViewModel by viewModels()
//        val data: Uri? = result.data?.data
//
//        // Check if payment was successful (simplified - in real app parse the URI properly)
//        val success = data?.getQueryParameter("status") == "success"
//        // Get transaction ID from the result
//        val transactionId = data?.getQueryParameter("txnId")
//            ?: data?.getQueryParameter("txnId")
//            ?: "unknown"
//        viewModel.handlePaymentResult(success,transactionId)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            Log.e("CRASH", "Uncaught exception", e)
        }

        setContent {
            LoginAppTheme {
                SetNavGraph(this)
            }
        }
    }
}