package com.appsv.loginapp.core.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appsv.loginapp.R
import com.appsv.loginapp.core.model.MailSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@Preview(showBackground = true)
//@Composable
//fun PreviewOtpScreen(modifier: Modifier = Modifier) {
//    OtpScreen(
//        email = "maheshkilaparthi14367@gmail.com"
//    ) { }
//}
@Composable
fun EmailOtpScreen(
    email: String,
    onOtpVerified: () -> Unit,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var otp by remember { mutableStateOf("") }
    var generatedOtp by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var isOtpExpired by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf(60) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }


    var isOtpTouched by rememberSaveable { mutableStateOf(false) }

    val isOtpEmpty by remember{ derivedStateOf { otp.isEmpty() || otp.length !=6 } }


    // Automatically send OTP when the screen is launched
    LaunchedEffect(Unit) {
        generatedOtp = generateOtp()
        coroutineScope.launch(Dispatchers.IO) {
            delay(2000)
            val subject = "Your OTP Code"
            val body = "Your OTP code is $generatedOtp"
            MailSender(email, subject, body).run()
        }
        otpSent = true
        feedbackMessage = "OTP sent successfully"
    }

    // Start the OTP expiry timer
    LaunchedEffect(key1=otpSent) {
        if (otpSent) {
            while (remainingTime > 0) {
                delay(1000)
                remainingTime--
            }
            isOtpExpired = true
            feedbackMessage = "OTP has expired.Request a new one."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "OTP Verification",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.login), // Use primary color from XML
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Email Input Field (Disabled)
        OutlinedTextField(
            value = email,
            onValueChange = { },
            label = { Text("Entered email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            textStyle = TextStyle(color = colorResource(id = R.color.onSurface)) // Use onSurface color
        )

        Spacer(modifier = Modifier.height(20.dp))

        // OTP Input Field
        OutlinedTextField(
            value = otp,
            onValueChange = {otp=it; isOtpTouched=true},
            label = { Text("Enter OTP*") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled =otpSent && !isOtpExpired,
            textStyle = TextStyle(color = colorResource(id = R.color.black)), // Use onSurface color
            supportingText = {
                if(isOtpTouched && isOtpEmpty){
                    Text(text = "entered otp should be 6-digit")
                }
            }
        )

        if (otpSent && !isOtpExpired) {
            // Show remaining time if OTP is sent and not expired
            Text(
                text = "OTP expires in $remainingTime seconds",
                color = if(remainingTime<31) colorResource(id = R.color.error) else colorResource(id=R.color.onSurface), // Use error color for expiry
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        // Feedback Message
        if (feedbackMessage != null) {
            Text(
                text = feedbackMessage!!,
                color = if (feedbackMessage!!.startsWith("OTP Verified") || feedbackMessage!!.startsWith("OTP sent successfully") || feedbackMessage!!.startsWith("New OTP sent successfully"))  colorResource(id = R.color.teal_200)
                else colorResource(id = R.color.error), // Use primary or error color
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Verify OTP Button
        Button(
            onClick = {
                if (isOtpExpired) {
                    feedbackMessage = "OTP has expired.Request a new one."
                } else if (otp == generatedOtp) {
                    feedbackMessage = "OTP Verified Successfully!"
                    onOtpVerified()
                } else {
                    feedbackMessage = "Invalid OTP! Try again."
                }
            },
            enabled = !isOtpEmpty,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.login), // Use primary color
                contentColor = colorResource(id = R.color.black) // Use onPrimary color
            )
        ) {
            Text("Verify OTP")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Resend OTP Button
        if (isOtpExpired) {
            otpSent=false
            Button(
                onClick = {
                    generatedOtp = generateOtp()
                    coroutineScope.launch(Dispatchers.IO) {
                        delay(2000)
                        val subject = "Your OTP Code"
                        val body = "Your OTP code is $generatedOtp"
                        MailSender(email, subject, body).run()
                    }
                    remainingTime=60
                    isOtpExpired=false
                    otpSent = true
                    feedbackMessage = "New OTP sent successfully"
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.login), // Use primary color
                    contentColor = colorResource(id = R.color.black) // Use onPrimary color
                )
            ) {
                Text("Resend OTP")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Cancel Button
        TextButton(
            onClick = {
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel", color = colorResource(id = R.color.login)) // Use primary color
        }
    }
}

// 🔹 Function to generate a random OTP
fun generateOtp(): String {
    return (100000..999999).random().toString()
}
