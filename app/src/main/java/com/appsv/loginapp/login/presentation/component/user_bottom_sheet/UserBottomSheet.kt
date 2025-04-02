package com.appsv.loginapp.login.presentation.component.user_bottom_sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.appsv.loginapp.login.domain.model.Users
import com.appsv.loginapp.login.domain.utils.PaymentStatus
import com.appsv.loginapp.login.presentation.common_components.common_component.FileViewerDialog
import com.appsv.loginapp.login.presentation.common_components.common_component.InfoGroup
import com.appsv.loginapp.login.presentation.common_components.common_component.InfoItem

@Composable
fun UserBottomSheet(user: Users) {

    var showBigSizeProfile by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Section
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (user.profilePic != null) {
                Image(
                    painter = rememberAsyncImagePainter(user.profilePic),
                    contentDescription = "Profile picture",
                    modifier = Modifier.fillMaxSize().clickable { showBigSizeProfile=true },
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Default profile",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Details Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Personal Info Group
            InfoGroup(title = "Personal Information") {
                InfoItem(label = "Name", value = user.name!!)
                InfoItem(label = "Date of Birth", value = user.dob!!)
                InfoItem(label = "Gender", value = user.gender!!)
                InfoItem(label = "Occupation", value = user.occupation!!)
            }

            // Account Info Group
            InfoGroup(title = "Account Information") {
                InfoItem(label = "Email", value = user.emailId!!)
                InfoItem(label = "Mobile Number", value = user.mobileNumber ?: "N/A")
            }

            // Facility Info Group
            InfoGroup(title = "Facility Details") {
                InfoItem(label = "Room No", value = user.roomNo ?: "N/A")
                InfoItem(label = "Facility", value = user.facility ?: "N/A")
                InfoItem(label = "PaymentStatus Status",
                    value = if (user.isPaid!! == PaymentStatus.PAID.name) "Paid" else "Pending")
            }

        }

    }
    if (showBigSizeProfile) {
        FileViewerDialog(
            fileUrl = user.profilePic, // Your String? file URL
            onDismiss = { showBigSizeProfile = false }
        )
    }
}

