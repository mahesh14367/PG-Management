package com.appsv.loginapp.login.presentation.admin_home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appsv.loginapp.R
import com.appsv.loginapp.login.domain.utils.AppRoute
import com.appsv.loginapp.login.presentation.common_components.common_component.EmptyState
import com.appsv.loginapp.login.presentation.component.BoysListScreen
import com.appsv.loginapp.login.presentation.component.FoodMenuScreen
import com.appsv.loginapp.login.presentation.component.GirlsListScreen
import com.appsv.loginapp.login.presentation.component.MessPayersScreen
import com.appsv.loginapp.login.presentation.component.StaffListScreen
import com.appsv.loginapp.login.presentation.component.day_menu.DayMenuContent

//@Preview(showBackground = true)
//@Composable
//fun PreviewPGHOmeScreen(modifier: Modifier = Modifier) {
//
//    PGHomeScreen(navController = rememberNavController(), pgViewModel = viewModel())
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    navController: NavController,
    pgViewModel: AdminScreenViewModel
) {

//    val isAdmin by pgViewModel.isAdmin.collectAsState()
//    if (!isAdmin) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Unauthorized Access", color = MaterialTheme.colorScheme.error)
//            Text("Only ADMIN users can access this page")
//        }
//        return
//    }else{
//          pgViewModel.performAdminAction()
//    }

    val pgScreenState by pgViewModel.state.collectAsState()
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    val scope = rememberCoroutineScope()

    // Search bar (similar to reference image)
    var selectedDay by remember { mutableStateOf<String?>(null) }
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.white_lite)),
        topBar = {
            AdminHomeScreenTopAppBar(selectedTab = selectedTab) { index ->
                selectedTab = index
            }
        },
        floatingActionButton = {
            Box(contentAlignment = Alignment.BottomEnd) {
                FloatingActionButton(
                    modifier = Modifier
                        .size(90.dp)
                        .padding(25.dp),
                    onClick = {
                        navController.navigate(AppRoute.SignUpScreen.route)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }

            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Search bar (similar to reference image)

            when (selectedTab) {
                0 -> {
                    BoysListScreen(navController = navController, pgScreenState = pgScreenState)
                }
                1 -> GirlsListScreen(navController = navController, pgScreenState = pgScreenState)
                2-> MessPayersScreen(navController = navController, pgScreenState = pgScreenState)
                3 -> StaffListScreen(navController = navController, pgScreenState = pgScreenState)
                4 -> FoodMenuScreen(days=days, selectedDay =  selectedDay) {it-> selectedDay=it;showBottomSheet = true }
                5 -> {
                    EmptyState(message = "Yet to implement...")
                }
            }


        }

    }
    // Bottom Sheet for Mess Menu
    if (showBottomSheet && selectedDay != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false;selectedDay = null },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            DayMenuContent(selectedDay!!)
        }
    }

}