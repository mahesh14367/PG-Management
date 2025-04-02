package com.appsv.loginapp.login.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.appsv.loginapp.R
import com.appsv.loginapp.login.domain.model.Users
import com.appsv.loginapp.login.domain.utils.Role
import com.appsv.loginapp.login.presentation.admin_home_screen.AdminScreenState
import com.appsv.loginapp.login.presentation.common_components.common_component.EmptyState
import com.appsv.loginapp.login.presentation.common_components.common_component.UserCard
import com.appsv.loginapp.login.presentation.common_components.common_component.UserProfileDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffListScreen(
    navController: NavController,
    pgScreenState: AdminScreenState
) {
    var currentUser by remember { mutableStateOf<Users?>(null) }
    var showUserProfileDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showFilterMenu by remember { mutableStateOf(false) }
    var selectedRoleFilter by remember { mutableStateOf<Role?>(null) }
    val focusManager = LocalFocusManager.current

    // Filter and search users
    val filteredUsers = pgScreenState.usersList.filter { user ->
        val matchesSearch = user.name?.contains(searchQuery, ignoreCase = true) == true ||
                user.emailId?.contains(searchQuery, ignoreCase = true) == true ||
                user.mobileNumber?.contains(searchQuery, ignoreCase = true) == true

        val matchesRole = selectedRoleFilter?.let { user.role == it.name } ?: true

        matchesSearch && matchesRole && user.role != Role.USER.name
    }

    Column {
        // Search and Filter Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search staff...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchQuery = ""
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                shape = RoundedCornerShape(12.dp),

            )

            Spacer(modifier = Modifier.width(8.dp))

            // Filter Button
            Box {
                IconButton(
                    onClick = { showFilterMenu = true },
                    modifier = Modifier.size(55.dp)
                        .background(
                            color = if (selectedRoleFilter != null) colorResource(id=R.color.black)
                            else colorResource(id=R.color.gray),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id=R.drawable.filter_list_icon),
                        contentDescription = "Filter",
                        tint = if (selectedRoleFilter != null) colorResource(id=R.color.gray)
                        else colorResource(id=R.color.black)
                    )
                }

                // Filter Dropdown Menu
                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("ALL ROLES") },
                        onClick = {
                            selectedRoleFilter = null
                            showFilterMenu = false
                        }
                    )
                    Role.entries.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role.name) },
                            onClick = {
                                selectedRoleFilter = role
                                showFilterMenu = false
                            }
                        )
                    }

                }
            }
        }

        // Selected Filter Chip
        if (selectedRoleFilter != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Filter: ${selectedRoleFilter!!.name}",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorResource(id=R.color.black),
                    modifier = Modifier
                        .background(
                            color = colorResource(id=R.color.gray),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier=Modifier.height(6.dp))
        // Content
        if (pgScreenState.isLoading) {
            Dialog(onDismissRequest = {}) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(12.dp))
                ) {
                    CircularProgressIndicator()
                }
            }
        } else if (filteredUsers.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(filteredUsers, key = { it.userId!! }) { user ->
                    UserCard(
                        user = user,
                        onClick = {
                            currentUser = user
                            showUserProfileDialog = true
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else {
            EmptyState(
                message = when {
                    searchQuery.isNotEmpty() -> "No staff members found for '$searchQuery'"
                    selectedRoleFilter != null -> "No ${selectedRoleFilter!!.name} staff members found"
                    else -> "No staff members available"
                }
            )
        }
    }

    if (showUserProfileDialog && currentUser != null) {
        UserProfileDialog(user = currentUser!!) { showUserProfileDialog = false }
    }
}