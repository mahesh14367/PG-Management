package com.appsv.loginapp.login.presentation.user_home_screen

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.appsv.loginapp.R
import com.appsv.loginapp.login.domain.utils.getFirstName
import com.appsv.loginapp.login.domain.utils.getTimeBasedGreeting
import com.appsv.loginapp.login.presentation.PaymentIntegration.PaymentViewModel
import com.appsv.loginapp.login.presentation.PaymentIntegration.PaymentScreen.PaymentBottomSheet
import com.appsv.loginapp.login.presentation.common_components.shared_view_model.SharedViewModel
import com.appsv.loginapp.login.presentation.component.FoodMenuScreen
import com.appsv.loginapp.login.presentation.component.announcement_components.AnnouncementCarousel
import com.appsv.loginapp.login.presentation.component.day_menu.DayMenuContent
import com.appsv.loginapp.login.presentation.component.user_bottom_sheet.UserBottomSheet


//@Preview(showBackground = true)
//@Composable
//fun PreviewMainScreen() {
//
//    UserHomeScreen()
//}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun UserHomeScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    paymentViewModel:PaymentViewModel,
    context: Context
) {
    val currentUser by sharedViewModel.user.collectAsState()

    // State for selected day and bottom sheet
    var selectedDay by remember { mutableStateOf<String?>(null) }
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showPaymentBottomSheet by remember{ mutableStateOf(false) }

    val userName= getFirstName(currentUser?.name)
    val updatedUserName by remember { mutableStateOf(getTimeBasedGreeting()) }


    Scaffold(
        topBar = {
            // Enhanced TopAppBar with better spacing and visual hierarchy
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = updatedUserName,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    // Profile picture with better touch target and status indicator
                    Box(modifier = Modifier.padding(start = 8.dp)) {
                        BadgedBox(
                            badge = {}
                        ) {
                            AsyncImage(
                                model = currentUser?.profilePic,
                                contentDescription = "Profile picture",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant,
                                        shape = CircleShape
                                    )
                                    .clickable { showBottomSheet=true },
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.person_icon),
                                error = painterResource(R.drawable.person_icon),
                            )
                        }
                    }
                },
                actions = {
                    // Add action icons with proper spacing
                    IconButton(
                        onClick = { /* Handle notification */ },
                        modifier = Modifier.size(48.dp)
                    ) {
                        BadgedBox(
                            badge = {
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        }
                    }
                },
                modifier = Modifier.shadow(elevation = 4.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Announcements Card (Auto-scrolling)
            Text(
                text = "Announcements",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            AnnouncementCarousel(
                announcements = listOf(
                    "Exam schedule changes starting next week",
                    "Library will remain closed on Friday",
                    "New sports facilities now available",
                    "Campus wifi upgrade scheduled for tomorrow",
                    "Student council elections next month"
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Days Selection Card
            Text(
                text = "Mess Menu",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            FoodMenuScreen(days=days, selectedDay = selectedDay) {it->
                selectedDay = it
                showBottomSheet = true
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Empty card as requested
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    TextButton(
                        onClick = {
                            showPaymentBottomSheet=true
                        }
                    ) {
                        Text(text = "PAY HERE")
                    }
                }
            }
        }
    }

    // Bottom Sheet for Mess Menu
    if (showBottomSheet && selectedDay != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false;selectedDay=null },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            DayMenuContent(selectedDay!!)
        }
    }
    if (showBottomSheet && selectedDay == null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            UserBottomSheet(currentUser!!)
        }
    }
    if(showPaymentBottomSheet){
        ModalBottomSheet(
            onDismissRequest = { showPaymentBottomSheet = false },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            PaymentBottomSheet(paymentViewModel=paymentViewModel,context=context){
                showPaymentBottomSheet=false
            }
        }
    }

}

