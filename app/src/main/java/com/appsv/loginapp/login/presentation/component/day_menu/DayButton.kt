package com.appsv.loginapp.login.presentation.component.day_menu

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.appsv.loginapp.R

@Composable
fun DayButton( modifier: Modifier,day: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = if (isSelected) {
            ButtonDefaults.buttonColors(
                containerColor = colorResource(id= R.color.gray),
            )
        } else {
            ButtonDefaults.buttonColors(
                containerColor = colorResource(id= R.color.login),
            )
        }
    ) {
        Text(
            text = day,
        )
    }
}