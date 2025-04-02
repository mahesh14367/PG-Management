import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.appsv.loginapp.R
import com.appsv.loginapp.login.domain.model.LoginCred
import com.appsv.loginapp.login.domain.utils.AppRoute
import com.appsv.loginapp.login.domain.utils.Role
import com.appsv.loginapp.login.presentation.common_components.shared_view_model.SharedViewModel
import com.appsv.loginapp.login.presentation.login_screen.Event
import com.appsv.loginapp.login.presentation.login_screen.LoginState
import com.appsv.loginapp.login.presentation.login_screen.LoginViewModel
import com.appsv.loginapp.login.presentation.signup_screen.component.SimpleAlertDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@Preview(showBackground = true)
@Composable
fun LoginScreen(
    navController: NavController,
    onClickForgetPassword: () -> Unit,
    viewModel: LoginViewModel,
    sharedViewModel: SharedViewModel
) {
    // State variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isEmailTouched by remember { mutableStateOf(false) }
    var isPasswordTouched by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }


    val isEmailEmpty by remember { derivedStateOf { email.isEmpty() } }
    val isPasswordEmpty by remember { derivedStateOf { password.isEmpty() } }

    val scope = rememberCoroutineScope()
    val loginState by viewModel.loginState.collectAsState()

    // Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }

    // Focus Management
    val (emailFocusRequester, passwordFocusRequester) = remember { FocusRequester.createRefs() }
    val focusManager = LocalFocusManager.current

    // Automatically focus the email field when the screen is launched
    LaunchedEffect(Unit) {
        emailFocusRequester.requestFocus()
    }

    // Handle login state changes
    when (val currentState = loginState) {
        is LoginState.Loading -> {
            Dialog(onDismissRequest = {}) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        // In LoginScreen.kt - Update the success handler
        is LoginState.Success -> {
            LaunchedEffect(currentState.user) {
                // Wait for role to be properly set in the database
                delay(500) // Additional safety delay

                when (currentState.user?.role) {
                    Role.ADMIN.name -> {
                        navController.navigate(AppRoute.AdminHome.route) {
                            popUpTo(AppRoute.LoginScreen.route) { inclusive = true }
                        }
                    }
                    Role.OWNER.name -> {
                        sharedViewModel.setUser(currentState.user)
                        navController.navigate(AppRoute.OwnerDashboard.route) {
                            popUpTo(AppRoute.LoginScreen.route) { inclusive = true }
                        }
                    }
                    else -> {
                        sharedViewModel.setUser(currentState.user!!)
                        navController.navigate(AppRoute.UserProfile.route) {
                            popUpTo(AppRoute.LoginScreen.route) { inclusive = true }
                        }
                    }
                }
                viewModel.onEvent(Event.LoginResetState)
            }
        }

        is LoginState.Error -> {
            SimpleAlertDialog(
                title = "LOGIN FAILED",
                text = "Reason: ${currentState.message}",
                confirmText = "",
                dismissText = "Ok",
                onConfirm = {},
                onDismiss = {
                    viewModel.onEvent(Event.LoginResetState)
                }
            ) {

            }
        }

        else -> {}
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.white))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.paying_guest),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )

            // Title
            Text(
                text = "Welcome Back",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.branda)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Login to continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.branda)
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; isEmailTouched = true },
                label = { Text("Email Address*") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocusRequester.requestFocus() } // Move focus to password field
                ),
                supportingText = {
                    if (isEmailTouched && isEmailEmpty) {
                        Text("Email shouldn't be empty!", color = Color.Red)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocusRequester), // Request focus for email field
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; isPasswordTouched = true },
                label = { Text("Password*") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password Icon"
                    )
                },
                trailingIcon = {
                    // Toggle between hide and show icon
                    IconButton(
                        onClick = { isPasswordVisible = !isPasswordVisible }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (isPasswordVisible) R.drawable.visibility_icon_on else R.drawable.visibility_icon_off
                            ),
                            contentDescription = if (isPasswordVisible) "Hide Password" else "Show Password",
                            tint = if (isPasswordEmpty) colorResource(id = R.color.white_lite) else colorResource(
                                id = R.color.login
                            )
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() } // Clear focus (hide keyboard)
                ),
                supportingText = {
                    if (isPasswordTouched && isPasswordEmpty) {
                        Text("Password shouldn't be empty!", color = Color.Red)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester), // Request focus for password field
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password
            TextButton(onClick = onClickForgetPassword) {
                Text(
                    text = "Forgot Password?",
                    color = colorResource(id = R.color.branda)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = {
                    val loginCred = LoginCred(email, password)
                    scope.launch {
                        viewModel.onEvent(Event.LoginCheck(loginCred))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isEmailEmpty && !isPasswordEmpty,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.login),
                    contentColor = Color.Black,
                    disabledContainerColor = colorResource(id = R.color.white_lite),
                    disabledContentColor = colorResource(id = R.color.white_lite)
                ),
                shape = CutCornerShape(12.dp)
            ) {
                Text("LOG IN")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Prompt
            TextButton(
                onClick = {
                    try {
                        navController.navigate(AppRoute.SignUpScreen.route) {
                            popUpTo(AppRoute.LoginScreen.route) { inclusive = true }
                        }
                    } catch (e: Exception) {
                        Log.e("NavigationError", "Failed to navigate to SignUpScreen: ${e.message}")
                        // Show a user-friendly error message
                        // Show a Snackbar with an error message
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Failed to navigate. Please try again.",
                                actionLabel = "Retry"
                            )
                        }
                    }
                }
            ) {
                Text(
                    text = "Don’t have an account? Sign up now",
                    color = colorResource(id = R.color.branda)
                )
            }
        }
    }
}