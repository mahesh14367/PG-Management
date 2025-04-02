package com.appsv.loginapp.login.presentation.common_components.common_component

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.appsv.loginapp.R
import com.appsv.loginapp.login.domain.model.Users
import com.appsv.loginapp.login.domain.utils.PaymentStatus
import com.appsv.loginapp.login.domain.utils.getFirstName

@Composable
fun UserCard(user: Users, onClick: () -> Unit) {

    val name= getFirstName(user.name)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile picture
            Image(
                painter = rememberAsyncImagePainter(
                    model = user.profilePic, // Use actual image URL from your user object
                    placeholder = painterResource(R.drawable.person_icon), // Add a default placeholder
                    error = painterResource(R.drawable.person_icon) // Add an error fallback
                ),
                contentDescription = "User Profile Pic",
                modifier = Modifier
                    .size(65.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Guest details
            Column{
                Row {
                    Text(
                        text = "Name:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row {
                    Text(
                        text = "Occupation:",
                        fontSize = 8.sp,
                        color = colorResource(id = R.color.gray_lite)
                    )
                    Text(
                        text = user.occupation!!,
                        fontSize = 8.sp,
                        color = colorResource(id = R.color.gray_lite)
                    )
                }

                Row {
                    Text(
                        text = "Room no:",
                        fontSize = 8.sp,
                        color = colorResource(id = R.color.gray_lite)
                    )
                    Text(
                        text = user.roomNo!!,
                        fontSize = 8.sp,
                        color = colorResource(id = R.color.gray_lite),
                    )
                }

            }

            Spacer(modifier = Modifier.weight(1f))

            // PaymentStatus status
            Box(
                modifier = Modifier
                    .background(
                        color = if (user.isPaid!! == PaymentStatus.PAID.name) colorResource(id = R.color.teal_200).copy(alpha = 0.2f)
                        else colorResource(id = R.color.error).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = if (user.isPaid == PaymentStatus.PAID.name) "Active" else "Inactive",
                    color = if (user.isPaid == PaymentStatus.PAID.name) colorResource(id = R.color.teal_200) else colorResource(
                        id = R.color.error
                    ),
                    fontSize = 8.sp
                )
            }
        }
    }


}