package com.appsv.loginapp.login.presentation.signup_screen.component

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.appsv.loginapp.R
import java.util.Calendar

@Composable
fun DateOfBirthPicker(
    modifier: Modifier,
    selectedDate: String,
    onNext:()->Unit,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onDateSelected(formattedDate)
        }, year, month, day
    )

    val isDateEmpty by remember  { derivedStateOf { selectedDate.isEmpty() } }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {  },
            readOnly = true,
            label = { Text("Date of Birth") },
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(
                        painter = painterResource(R.drawable.date_range_icon),
                        contentDescription = "Select Date"
                    )
                }
            },
            keyboardActions = KeyboardActions(
                onNext = { onNext() } // Move focus to email field
            ),
            supportingText = {
                when{
                    isDateEmpty->{
                        Text(text = "DOB shouldn't be empty")
                    }
                }
            },
            modifier = modifier
        )
    }
}
