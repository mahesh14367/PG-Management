import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.appsv.loginapp.R
import com.appsv.loginapp.core.presentation.EmailOtpScreen
import com.appsv.loginapp.login.domain.model.SignUpCred
import com.appsv.loginapp.login.domain.model.Users
import com.appsv.loginapp.login.domain.utils.AppRoute
import com.appsv.loginapp.login.domain.utils.Facility
import com.appsv.loginapp.login.domain.utils.Gender
import com.appsv.loginapp.login.domain.utils.Occupation
import com.appsv.loginapp.login.domain.utils.Role
import com.appsv.loginapp.login.presentation.login_screen.Event
import com.appsv.loginapp.login.presentation.login_screen.LoginViewModel
import com.appsv.loginapp.login.presentation.signup_screen.SignUpState
import com.appsv.loginapp.login.presentation.signup_screen.component.DateOfBirthPicker
import com.appsv.loginapp.login.presentation.signup_screen.component.FilePreviewItem
import com.appsv.loginapp.login.presentation.signup_screen.component.SimpleAlertDialog
import com.appsv.loginapp.login.presentation.signup_screen.upload_state.FileUploadState
import com.appsv.loginapp.login.presentation.signup_screen.upload_state.ImageUploadState
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen(modifier: Modifier = Modifier) {
    SignUpScreen(
        navController = rememberNavController(),
        viewModel = viewModel()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    // State variables
    var name by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf(Gender.MALE.name) }
    var stayOption by rememberSaveable { mutableStateOf(Facility.STAY.name) } // "Stay", "Mess", "Both"
    var roomNo by rememberSaveable { mutableStateOf("") }
    var occupation by rememberSaveable { mutableStateOf(Occupation.STUDENT.name) }

    var imageUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var fileUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var selectedFileUri by rememberSaveable { mutableStateOf<Uri?>(null) }


    val isImageUploaded by remember { derivedStateOf { (imageUrl ?: "").isNotEmpty() } }
    val isFileUploaded by remember { derivedStateOf { (fileUrl ?: "").isNotEmpty() } }


    var isNameTouched by remember { mutableStateOf(false) }
    var isEmailTouched by remember { mutableStateOf(false) }
    var isPhoneTouched by remember { mutableStateOf(false) }
    var isPasswordTouched by remember { mutableStateOf(false) }
    var isConfirmPasswordTouched by remember { mutableStateOf(false) }
    var isFileUploadIconTouched by remember { mutableStateOf(false) }
    var isImageIconTouched by remember { mutableStateOf(false) }
    var isRoomNoTouched by remember { mutableStateOf(false) }
    var isOccupationExpanded by remember { mutableStateOf(false) }


    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    var isEmailVerified by remember { mutableStateOf(false) } // Track email verification status
//    var isPhoneVerified by remember { mutableStateOf(false) } // Track phone number verification status
    var showEmailOtpDialog by remember { mutableStateOf(false) } // Control Email OTP dialog visibility
//    var showPhoneOtpDialog by remember { mutableStateOf(false) } // Control Phone OTP dialog visibility

    val isDobEmpty by remember { derivedStateOf { dob.isEmpty() } }
    val isNameEmpty by remember { derivedStateOf { name.isEmpty() } }
    val isEmailEmpty by remember { derivedStateOf { email.isEmpty() } }
    val isPhoneEmpty by remember { derivedStateOf { phoneNumber.isEmpty() } }
    val isPasswordEmpty by remember { derivedStateOf { password.isEmpty() } }
    val isConfirmPasswordEmpty by remember { derivedStateOf { confirmPassword.isEmpty() } }
    val isPasswordMismatch by remember { derivedStateOf { password != confirmPassword } }
    val isRoomNoEmpty by remember { derivedStateOf { roomNo.isEmpty() } }


    val NAME_REGEX = Regex("^[A-Za-z '-]+$")
    val EMAIL_REGEX =
        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$", RegexOption.IGNORE_CASE)
    val PHONE_REGEX = Regex("^\\+91[6-9][0-9]{9}$")
    val PASSWORD_REGEX = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\$%^&*]).{6,}\$")


    var isNameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPhoneError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val signUpState by viewModel.signUpState.collectAsState()
    val imageUploadState by viewModel.imageUploadState.collectAsState()
    val fileUploadState by viewModel.fileUploadState.collectAsState()
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                scope.launch {
                    try {
                        viewModel.uploadImageToCloudinary(context, it)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    val fileLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedFileUri = it
                scope.launch {
                    try {
                        viewModel.uploadFileToCloudinary(context, it)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    val verticalScrollState = rememberScrollState()
//    val horizontalScrollState = rememberScrollState()

    // Focus Management
    val (nameFocusRequester, dobFocusRequester, roomFocusRequester, emailFocusRequester, phoneFocusRequester, passwordFocusRequester, confirmPasswordFocusRequester) = remember {
        FocusRequester.createRefs()
    }
    val focusManager = LocalFocusManager.current

    // Automatically focus the name field when the screen is launched
    LaunchedEffect(Unit) {
        nameFocusRequester.requestFocus()
    }

    LaunchedEffect(imageUploadState) {
        if (imageUploadState is ImageUploadState.Success) {
            imageUrl = (imageUploadState as ImageUploadState.Success).imageUrl
        }
    }

    LaunchedEffect(fileUploadState) {
        if (fileUploadState is FileUploadState.Success) {
            fileUrl = (fileUploadState as FileUploadState.Success).fileUrl
        }
    }

    // Handle sign-up state changes

    when (val state = signUpState) {
        is SignUpState.Loading -> {
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

        is SignUpState.Success -> {
            SimpleAlertDialog(
                title = "SIGNUP SUCCESSFUL!",
                text = "Now you can login using these credentials: Email: $email, Password: $password",
                confirmText = "",
                dismissText = "Ok",
                onConfirm = {},
                onDismiss = {
                    scope.launch {
                        viewModel.onEvent(Event.SignUpResetState)
                        viewModel.resetBothImageandFileUploadState()

//                        navController.navigate(AppRoute.AdminHome.route) {
//                            popUpTo(AppRoute.SignUpScreen.route) { inclusive = true }
//                        }
                    }
                }
            ) {

            }
        }

        is SignUpState.Error -> {
            SimpleAlertDialog(
                title = "SIGNUP FAILED",
                text = "Reason: ${state.message}",
                confirmText = "",
                dismissText = "Ok",
                onConfirm = {},
                onDismiss = {
                    scope.launch {
                        viewModel.onEvent(Event.SignUpResetState)
                    }
                }
            ) {

            }
        }

        else -> {}
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
            .background(colorResource(id = R.color.white))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        // App Logo
//        Image(
//            painter = painterResource(id = R.drawable.paying_guest),
//            contentDescription = "App Logo",
//            modifier = Modifier
//                .size(150.dp)
//                .padding(bottom = 16.dp)
//        )
        Spacer(modifier = Modifier.height(22.dp))
        // Title
        Text(
            text = "Create An Account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.branda)
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Display selected image
        selectedImageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Uploaded Image",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Upload Button
        IconButton(
            onClick = {
                isImageIconTouched = true
                launcher.launch("image/*")
            },
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                painter = painterResource(R.drawable.add_image_icon),
                contentDescription = "Select Image",
                tint = colorResource(R.color.black)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // image uploading status
        when (imageUploadState) {
            is ImageUploadState.Loading -> CircularProgressIndicator()
            is ImageUploadState.Success -> Text(
                "Image Uploaded Successfully!",
                color = colorResource(id = R.color.teal_200)
            )

            is ImageUploadState.Error -> Text(
                "Upload Failed!",
                color = colorResource(id = R.color.error)
            )

            else -> {}
        }

        if (isImageIconTouched && !isImageUploaded) {
            Text("Must upload the profile!", color = colorResource(id = R.color.error))
        }

        // Name Field
        OutlinedTextField(
            value = name,
            onValueChange = { it ->
                name = it
                isNameTouched = true
                isNameError = !name.matches(NAME_REGEX)
            },
            label = { Text("Name*") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.person_icon),
                    contentDescription = "Person Icon"
                )
            },
            trailingIcon = {
                if (isNameError) {
                    Icon(
                        painter = painterResource(id = R.drawable.warning_icon),
                        contentDescription = "Error",
                        tint = colorResource(id = R.color.error)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { dobFocusRequester.requestFocus() } // Move focus to email field
            ),
            supportingText = {
                when {
                    isNameTouched && isNameEmpty -> {
                        Text(
                            text = "Name shouldn't be empty!",
                            color = colorResource(id = R.color.error)
                        )
                    }

                    isNameTouched && isNameError -> {
                        Text(
                            text = "Invalid name format. Only letters, spaces, hyphens, and apostrophes are allowed.",
                            color = colorResource(id = R.color.error),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            isError = isNameError || (isNameTouched && isNameEmpty),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nameFocusRequester),// Request focus for email field
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        //Date of birth field
        DateOfBirthPicker(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(dobFocusRequester),
            selectedDate = dob,
            onNext = {
                emailFocusRequester.requestFocus()
            }
        ) { selected ->
            dob =
                selected  // Here, "onDateSelected" is actually the lambda (selected -> dob = selected)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { it ->
                email = it.lowercase()
                isEmailTouched = true
                isEmailError = !email.matches(EMAIL_REGEX)
            },
            label = { Text("Email Address*") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon"
                )
            },
            trailingIcon = {
                when {
                    isEmailVerified -> {
                        Text("Verified", color = colorResource(id = R.color.teal_200))
                    }

                    isEmailError -> {
                        Icon(
                            painter = painterResource(id = R.drawable.warning_icon),
                            contentDescription = "Error",
                            tint = colorResource(id = R.color.error)
                        )
                    }

                    else -> {
                        TextButton(
                            onClick = { showEmailOtpDialog = true },
                            enabled = !isEmailEmpty
                        ) {
                            Text("Verify")
                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { phoneFocusRequester.requestFocus() } // Move focus to password field
            ),
            supportingText = {
                when {
                    isEmailTouched && isEmailEmpty -> {
                        Text(
                            "Email shouldn't be empty!", color = colorResource(id = R.color.error)
                        )
                    }

                    isEmailTouched && isEmailError -> {
                        Text(
                            text = "Email format should be like this: xyz@something.com",
                            color = colorResource(id = R.color.error),
                            style = MaterialTheme.typography.bodySmall
                        )

                    }
                }
            },
            isError = isEmailError || (isEmailTouched && isEmailEmpty),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocusRequester),
            enabled = !isEmailVerified,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Mobile Number field
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { it ->
                phoneNumber = it.lowercase()
                isPhoneTouched = true
                isPhoneError = !phoneNumber.matches(PHONE_REGEX)
            },
            label = { Text("Mobile Number*") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone Icon"
                )
            },
            trailingIcon = {
                when {
//                    isPhoneVerified -> {
//                        Text("Verified", color = colorResource(id = R.color.teal_200))
//                    }

                    isPhoneError -> {
                        Icon(
                            painter = painterResource(id = R.drawable.warning_icon),
                            contentDescription = "Error",
                            tint = colorResource(id = R.color.error)
                        )
                    }

                    else -> {
//                        TextButton(
//                            onClick = { showPhoneOtpDialog = true },
//                            enabled = !isPhoneEmpty
//                        ) {
//                            Text("Verify")
//                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() } // Move focus to password field
            ),
            supportingText = {
                when {
                    isPhoneTouched && isPhoneEmpty -> {
                        Text(
                            "Mobile number shouldn't be empty!",
                            color = colorResource(id = R.color.error)
                        )
                    }

                    isPhoneTouched && isPhoneError -> {
                        Text(
                            text = "Mobile number format should be like this: +91xxxxxxxxxx",
                            color = colorResource(id = R.color.error),
                            style = MaterialTheme.typography.bodySmall
                        )

                    }
                }
            },
            isError = isPhoneError || (isPhoneTouched && isPhoneEmpty),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(phoneFocusRequester),
            enabled = true,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { it ->
                password = it
                isPasswordTouched = true
                isPasswordError = !password.matches(PASSWORD_REGEX)
            },
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
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { confirmPasswordFocusRequester.requestFocus() } // Move focus to password field
            ),
            supportingText = {
                when {
                    isPasswordTouched && isPasswordEmpty -> {
                        Text(
                            text = "Password shouldn't be empty!",
                            color = colorResource(id = R.color.error)
                        )
                    }

                    isPasswordTouched && isPasswordError -> {
                        Text(
                            text = "Password must be at least 6 characters long and include:\n- One uppercase letter\n- One lowercase letter\n- One digit\n- One special character (!@#\$%^&*)",
                            color = colorResource(id = R.color.error),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 3.dp, top = 5.dp)
                        )
                    }
                }

            },
            isError = isPasswordError || (isPasswordTouched && isPasswordEmpty),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it; isConfirmPasswordTouched = true },
            label = { Text("Confirm Password*") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Confirm Password Icon"
                )
            },
            trailingIcon = {
                // Toggle between hide and show icon
                IconButton(
                    onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isConfirmPasswordVisible) R.drawable.visibility_icon_on else R.drawable.visibility_icon_off
                        ),
                        contentDescription = if (isConfirmPasswordVisible) "Hide Password" else "Show Password",
                        tint = if (isConfirmPasswordEmpty) colorResource(id = R.color.white_lite) else colorResource(
                            id = R.color.login
                        )
                    )
                }
            },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() } // Clear focus (hide keyboard)
            ),
            supportingText = {
                if (isConfirmPasswordTouched) {
                    if (isConfirmPasswordEmpty) {
                        Text(
                            "Confirm Password shouldn't be empty!",
                            color = colorResource(id = R.color.error)
                        )
                    } else if (isPasswordMismatch) {
                        Text("Passwords do not match!", color = colorResource(id = R.color.error))
                    }
                }
            },
            isError = (isConfirmPasswordTouched && isPasswordMismatch) || (isConfirmPasswordTouched && isConfirmPasswordEmpty),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(confirmPasswordFocusRequester),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(12.dp))

        // Room Number Field
        OutlinedTextField(
            value = roomNo,
            onValueChange = {
                if (it.length <= 2) {
                    roomNo = it
                }
                isRoomNoTouched = true
            },
            label = { Text("Room Number*") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.bed_icon),
                    contentDescription = "Room Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { emailFocusRequester.requestFocus() } // Move focus to email field
            ),
            singleLine = true,
            isError = isRoomNoTouched && isRoomNoEmpty,
            supportingText = {
                if (isRoomNoTouched && isRoomNoEmpty) {
                    Text(
                        "Room number is required",
                        color = colorResource(id = R.color.error)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(roomFocusRequester)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Occupation Dropdown

        ExposedDropdownMenuBox(
            expanded = isOccupationExpanded,
            onExpandedChange = { isOccupationExpanded = !isOccupationExpanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = occupation,
                onValueChange = {},
                label = { Text("Occupation*") },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isOccupationExpanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                singleLine = true
            )

            ExposedDropdownMenu(
                expanded = isOccupationExpanded,
                onDismissRequest = { isOccupationExpanded = false },
                modifier = Modifier.exposedDropdownSize(matchTextFieldWidth = true)
            ) {
                Occupation.entries.forEach { occupationOption ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = occupationOption.name,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {
                            occupation = occupationOption.name
                            isOccupationExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Gender Radio Buttons
        Text(
            text = "Gender*",
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(id = R.color.onSurface)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                RadioButton(
                    selected = gender == Gender.MALE.name,
                    onClick = { gender = Gender.MALE.name }
                )
                Text(
                    "Male",
                    modifier = Modifier.padding(start = 4.dp),
                    color = colorResource(id = R.color.black)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                RadioButton(
                    selected = gender == Gender.FEMALE.name,
                    onClick = { gender = Gender.FEMALE.name }
                )
                Text(
                    "Female",
                    modifier = Modifier.padding(start = 4.dp),
                    color = colorResource(id = R.color.black)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))


        // Stay/Mess Options
        Text(
            text = "Stay & Mess Option*",
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(id = R.color.onSurface)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = stayOption == Facility.STAY.name,
                onClick = { stayOption = Facility.STAY.name },
                label = { Text("Stay(Including mess)") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = stayOption == Facility.MESS.name,
                onClick = { stayOption = Facility.MESS.name },
                label = { Text("Only Mess") },
                modifier = Modifier.weight(1f)
            )
//            FilterChip(
//                selected = stayOption == "Both",
//                onClick = { stayOption = "Both" },
//                label = { Text("Both") },
//                modifier = Modifier.weight(1f)
//            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Select Files", color = colorResource(id = R.color.black))

        Spacer(modifier = Modifier.width(8.dp))

        // Button to select files
        IconButton(
            onClick = {
                try {
                    isFileUploadIconTouched = true
                    fileLauncher.launch("*/*") // Explicit array for MIME types
                } catch (e: Exception) {
                    Toast.makeText(context, "Error opening file manager", Toast.LENGTH_SHORT).show()
                    Log.e("FileUpload", "Error: ${e.message}")
                }
            },
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                painter = painterResource(R.drawable.upload_file_icon),
                contentDescription = "Upload File",
                tint = colorResource(R.color.black)
            )
        }

        selectedFileUri?.let { it ->
            FilePreviewItem(uri = it)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // image uploading status
        when (fileUploadState) {
            is FileUploadState.Loading -> CircularProgressIndicator()
            is FileUploadState.Success -> Text(
                "File Uploaded Successfully!",
                color = colorResource(id = R.color.teal_200)
            )

            is FileUploadState.Error -> Text(
                "Upload Failed!",
                color = colorResource(id = R.color.error)
            )

            else -> {}
        }

        if (isFileUploadIconTouched && !isFileUploaded) {
            Text("Must upload the ID proof!", color = colorResource(id = R.color.error))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sign Up Button (updated to use isSignUpEnabled)
        Button(
            onClick = {
                val signUpCred = SignUpCred(
                    emailId = email,
                    password = password
                )
                val user = Users(
                    name = name,
                    profilePic = imageUrl,
                    dob = dob,
                    emailId = email,
                    mobileNumber = phoneNumber,
                    password = password,
                    idProofDoc = fileUrl,
                    facility = stayOption,
                    roomNo = roomNo,
                    gender = gender,
                    occupation = occupation
                )
                scope.launch {
                    viewModel.onEvent(Event.SaveSignUpCred(signUpCred, user))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isImageUploaded &&
                    !isDobEmpty &&
                    !isNameEmpty &&
                    !isEmailEmpty &&
                    !isPhoneEmpty &&
                    !isPasswordEmpty &&
                    !isConfirmPasswordEmpty &&
                    !isPasswordMismatch &&
                    isEmailVerified &&
                    isFileUploaded &&
                    !isRoomNoEmpty &&
                    !isNameError &&
                    !isEmailError &&
                    !isPasswordError &&
                    !isPhoneError
        ) {
            Text("SIGN UP")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Login Prompt
        TextButton(
            onClick = {
                navController.popBackStack()
                navController.navigate(AppRoute.LoginScreen.route)
            }
        ) {
            Text(
                text = "Have an account already? Log in",
                color = colorResource(id = R.color.branda)
            )
        }
    }

    if (showEmailOtpDialog) {
        Dialog(
            onDismissRequest = {
                showEmailOtpDialog = false
            }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth() // Take 90% of the screen width
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                color = colorResource(id = R.color.surface) // Use surface color from XML
            ) {
                EmailOtpScreen(
                    email = email.trim(),
                    onOtpVerified = {
                        isEmailVerified = true
                        showEmailOtpDialog = false
                    },
                    onDismiss = {
                        showEmailOtpDialog = false
                    }
                )
            }
        }
    }
//    if (showPhoneOtpDialog) {
//        Dialog(
//            onDismissRequest = {
//                showPhoneOtpDialog = false
//            }
//        ) {
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth() // Take 90% of the screen width
//                    .padding(16.dp),
//                shape = RoundedCornerShape(12.dp),
//                color = colorResource(id = R.color.surface) // Use surface color from XML
//            ) {
//                PhoneOtpScreen(
//                    phoneNumber = phoneNumber.trim(),
//                    onOtpVerified = {
//                        isPhoneVerified = true
//                        showPhoneOtpDialog = false
//                    },
//                    onDismiss = {
//                        showPhoneOtpDialog = false
//                    }
//                )
//            }
//        }
//    }

}
