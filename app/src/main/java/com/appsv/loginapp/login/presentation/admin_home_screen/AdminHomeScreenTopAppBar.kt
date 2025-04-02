package com.appsv.loginapp.login.presentation.admin_home_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appsv.loginapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreenTopAppBar(
    selectedTab:Int,
    onClickTab:(Int)->Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Admin Dashboard",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.login),
                titleContentColor = colorResource(id = R.color.gray_lite)
            )
        )

        // Tabs
        val tabs = listOf("Boys", "Girls", "Mess_Payers", "Staff", "Menu", "Summary")


        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 0.dp,
            modifier = Modifier.fillMaxWidth(),
            containerColor = colorResource(id = R.color.login),
            contentColor = colorResource(id = R.color.gray_lite),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    height = 2.dp,
                    color =colorResource(id = R.color.gray_lite)
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
                    selectedContentColor = colorResource(id = R.color.gray_lite),
                    unselectedContentColor = colorResource(id = R.color.gray).copy(alpha = 0.6f)
                )
            }
        }
    }
}