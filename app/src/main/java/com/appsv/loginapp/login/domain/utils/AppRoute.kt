package com.appsv.loginapp.login.domain.utils

sealed class AppRoute(val route: String) {
    object AdminHome : AppRoute("admin_home")
    object OwnerDashboard : AppRoute("owner_dashboard")
    object UserProfile : AppRoute("user_profile")
    object LoginScreen : AppRoute("login_screen")
    object SignUpScreen:AppRoute(route = "signUp_screen")
}