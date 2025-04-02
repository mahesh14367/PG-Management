package com.appsv.loginapp.login.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appsv.loginapp.login.presentation.component.day_menu.DayButton

@Composable
fun FoodMenuScreen(
    days:List<String>,
    selectedDay:String?,
    onClick:(String)->Unit
)
{
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 64.dp), // Adjust minSize as needed
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(days) { day ->
                    DayButton(
                        day = day,
                        isSelected = selectedDay == day,
                        onClick = {
                           onClick(day)
                        },
                        modifier = Modifier
                            .height(48.dp) // Fixed height for consistency
                            .fillMaxWidth() // Each button takes equal width
                    )
                }
            }
        }
    }
}
