package com.appsv.loginapp.login.presentation.common_components.common_component

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.appsv.loginapp.login.domain.model.Users
import com.appsv.loginapp.login.domain.utils.PaymentStatus

//@Preview(showBackground = true)
//@Composable
//private fun PreviewDialog() {
//    UserProfileDialog(user = Users(
//        name = "mahesh",
//        profilePic = "imageUrl",
//        dob = "12/4/2003",
//        emailId = "mahesh@dld.com",
//        password = "123445",
//        idProofDoc = "fileUrl",
//        facility = "Stay",
//        roomNo = "12",
//        gender = "Male",
//        occupation = "STUDENT"
//    )) { }
//}
@Composable
fun UserProfileDialog(
    user: Users, // Your Users data class
    onDismiss: () -> Unit
) {

    var showFileViewerDialog by remember { mutableStateOf(false) }
    var showBigSizeProfile by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
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
                        InfoItem(label = "Role", value = user.role!!)
                    }

                    // Facility Info Group
                    InfoGroup(title = "Facility Details") {
                        InfoItem(label = "Room No", value = user.roomNo ?: "N/A")
                        InfoItem(label = "Facility", value = user.facility ?: "N/A")
                        InfoItem(label = "PaymentStatus Status",
                            value = if (user.isPaid!! == PaymentStatus.PAID.name) "Paid" else "Pending")
                    }

                    // ID Proof (if available)
                    if (user.idProofDoc != null) {
                        Button(
                            onClick = { showFileViewerDialog=true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        ) {
                            Text("View ID Proof")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Close Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Close")
                }
            }
        }
    }
    if (showFileViewerDialog) {
        FileViewerDialog(
            fileUrl = user.idProofDoc, // Your String? file URL
            onDismiss = { showFileViewerDialog = false }
        )
    }

    if (showBigSizeProfile) {
        FileViewerDialog(
            fileUrl = user.profilePic, // Your String? file URL
            onDismiss = { showBigSizeProfile = false }
        )
    }

}
