package com.appsv.loginapp.login.presentation.nav_graph

import LoginScreen
import SignUpScreen
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appsv.loginapp.login.domain.utils.AppRoute
import com.appsv.loginapp.login.presentation.PaymentIntegration.PaymentViewModel
import com.appsv.loginapp.login.presentation.login_screen.LoginViewModel
import com.appsv.loginapp.login.presentation.admin_home_screen.AdminScreen
import com.appsv.loginapp.login.presentation.admin_home_screen.AdminScreenViewModel
import com.appsv.loginapp.login.presentation.common_components.shared_view_model.SharedViewModel
import com.appsv.loginapp.login.presentation.owner_dash_board.OwnerDashBoard
import com.appsv.loginapp.login.presentation.user_home_screen.UserHomeScreen


@Composable
fun SetNavGraph(context: Context) {
    val navController= rememberNavController()

    val viewModel: LoginViewModel = viewModel()
    val sharedViewModel:SharedViewModel=viewModel()
    val paymentViewModel: PaymentViewModel = viewModel()


    NavHost(navController=navController, startDestination = AppRoute.LoginScreen.route){
        composable(route = AppRoute.LoginScreen.route) {
            LoginScreen(
                navController=navController,
                onClickForgetPassword = {},
                viewModel=viewModel,
                sharedViewModel=sharedViewModel
            )

        }

        composable(route = AppRoute.SignUpScreen.route) {
            SignUpScreen(
                navController=navController,
                viewModel=viewModel
            )
        }

        composable(route = AppRoute.AdminHome.route){
            val pgViewModel:AdminScreenViewModel=viewModel()
            AdminScreen(
                navController=navController,
                pgViewModel=pgViewModel,
            )
        }
        composable(route = AppRoute.OwnerDashboard.route){
            val pgViewModel:AdminScreenViewModel=viewModel()
            OwnerDashBoard(
                navController=navController,
                pgViewModel=pgViewModel,
                sharedViewModel=sharedViewModel
            )
        }
        composable(route = AppRoute.UserProfile.route){
            UserHomeScreen(
                navController=navController,
                sharedViewModel=sharedViewModel,
                paymentViewModel=paymentViewModel,
                context=context
            )
        }


    }

}



