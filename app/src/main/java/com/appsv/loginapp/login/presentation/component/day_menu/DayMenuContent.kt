package com.appsv.loginapp.login.presentation.component.day_menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DayMenuContent(day: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        val updatedDay = when (day) {
            "Sun"-> "Sunday"
            "Mon"-> "Monday"
            "Tue"-> "Tuesday"
            "Wed"-> "Wednesday"
            "Thu"-> "Thursday"
            "Fri"-> "Friday"
            "Sat"-> "Saturday"
            else -> ""
        }
        Text(
            text = "$updatedDay Menu",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sample menu items - you would replace with your actual data
        val menuItems = when (day) {
            "Mon" -> listOf("Breakfast: Poha", "Lunch: Dal Rice", "Dinner: Roti Sabzi")
            "Tue" -> listOf("Breakfast: Sandwich", "Lunch: Rajma Rice", "Dinner: Paneer")
            "Wed" -> listOf("Breakfast: Idli", "Lunch: Sambar Rice", "Dinner: Chole")
            "Thu" -> listOf("Breakfast: Paratha", "Lunch: Kadhi Rice", "Dinner: Dal Fry")
            "Fri" -> listOf("Breakfast: Upma", "Lunch: Veg Biryani", "Dinner: Khichdi")
            "Sat" -> listOf("Breakfast: Dosa", "Lunch: Fried Rice", "Dinner: Noodles")
            "Sun" -> listOf("Breakfast: Puri Sabzi", "Lunch: Special Thali", "Dinner: Pav Bhaji")
            else -> emptyList()
        }

        menuItems.forEach { item ->
            Text(
                text = "• $item",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Handle action */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Request Change")
        }
    }
}