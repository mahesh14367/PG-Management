package com.appsv.loginapp.login.presentation.common_components.common_component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appsv.loginapp.R
import com.appsv.loginapp.login.domain.model.Staff

@Composable
fun StaffCard(staff: Staff,onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile picture
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(colorResource(id= R.color.gray_lite)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = staff.name.split(" ").map { it.first() }.joinToString(""),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Staff details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = staff.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = staff.position,
                    fontSize = 14.sp,
                    color = colorResource(id= R.color.gray_lite)
                )
                Text(
                    text = "Shift: ${staff.shift}",
                    fontSize = 12.sp,
                    color = colorResource(id= R.color.gray_lite)
                )
            }
        }
    }
}