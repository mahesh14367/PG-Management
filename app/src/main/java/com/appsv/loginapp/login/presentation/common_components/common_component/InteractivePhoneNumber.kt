package com.appsv.loginapp.login.presentation.common_components.common_component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InteractivePhoneNumber(
    phoneNumber: String,
    modifier: Modifier = Modifier,
    showAsLink: Boolean = true
) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        if (showAsLink) {
            withStyle(style = SpanStyle(
                color = androidx.compose.ui.graphics.Color.Blue,
                textDecoration = TextDecoration.Underline
            )) {
                append(phoneNumber)
            }
        } else {
            append(phoneNumber)
        }
    }

    Text(
        text = annotatedText,
        modifier = modifier.combinedClickable(
            onClick = { /* Regular click action (optional) */ },
            onLongClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                context.startActivity(intent)
            }
        ),
        fontSize = 16.sp
    )
}