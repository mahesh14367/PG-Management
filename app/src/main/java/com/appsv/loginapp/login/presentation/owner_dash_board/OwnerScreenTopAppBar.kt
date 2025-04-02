package com.appsv.loginapp.login.presentation.owner_dash_board

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.appsv.loginapp.R
import com.appsv.loginapp.login.domain.model.Users
import com.appsv.loginapp.login.domain.utils.getFirstName
import com.appsv.loginapp.login.domain.utils.getTimeBasedGreeting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerScreenTopAppBar(
    selectedTab: Int,
    currentUser: Users?,
    onClickProfile:()->Unit,
    onClickTab: (Int) -> Unit
) {
    Column {
        // Enhanced TopAppBar with better spacing and visual hierarchy
        CenterAlignedTopAppBar(
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = getTimeBasedGreeting(),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = getFirstName(currentUser?.name),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(id=R.color.white),
                titleContentColor = colorResource(id=R.color.black),
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
                                .clickable { onClickProfile() },
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.person_icon),
                            error = painterResource(R.drawable.person_icon),
                        )
                    }
                }
            },
            modifier = Modifier.shadow(elevation = 4.dp)
        )

        // Tabs
        val tabs = listOf("Boys", "Girls", "Mess_Payers", "Staff", "Menu", "Summary")


        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 0.dp,
            modifier = Modifier.fillMaxWidth(),
            containerColor = colorResource(id = R.color.white),
            contentColor = colorResource(id = R.color.branda),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    height = 2.dp,
                    color =colorResource(id = R.color.login)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        onClickTab(index)
                    },
                    text = { Text(title) },
                    selectedContentColor = colorResource(id = R.color.login),
                    unselectedContentColor = colorResource(id = R.color.black).copy(alpha = 0.6f)
                )
            }
        }
    }
}