package com.appsv.loginapp.login.data.repository

import android.util.Log
import com.appsv.loginapp.core.data.UsersDTO
import com.appsv.loginapp.core.domain_mapper.toUsers
import com.appsv.loginapp.login.domain.model.LoginCred
import com.appsv.loginapp.login.domain.model.SignUpCred
import com.appsv.loginapp.login.domain.model.Users
import com.appsv.loginapp.login.domain.repository.AuthenticationRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates

class AuthenticationRepositoryImpl : AuthenticationRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val firebaseDataBase = FirebaseDatabase.getInstance().getReference("Users")

    override suspend fun saveSignUpCred(signUpCred: SignUpCred,user:Users): Result<FirebaseUser> {
        return try {
            val authResult =
                firebaseAuth.createUserWithEmailAndPassword(signUpCred.emailId, signUpCred.password)
                    .await()

            val userId = authResult.user?.uid
            Log.d("SignUp", "User created successfully: $userId")
            val userDTO=UsersDTO(
                userId=userId,
                profilePic=user.profilePic,
                name=user.name,
                dob=user.dob,
                emailId=user.emailId,
                mobileNumber = user.mobileNumber,
                password=user.password,
                role=user.role,
                idProofDoc=user.idProofDoc,
                facility  =user.facility,
                roomNo=user.roomNo,
                isPaid=user.isPaid,
                gender=user.gender,
                occupation=user.occupation
            )
            firebaseDataBase.child(userId!!).setValue(userDTO)
            Result.success(authResult.user!!)
        } catch (e: FirebaseAuthWeakPasswordException) {
            Log.e("SignUp", "Weak password: ${e.message}")
            Result.failure(e)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e("SignUp", "Invalid credentials: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("SignUp", "Unexpected error: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun authenticate(loginCred: LoginCred): Result<Users?> {
        return try {
            val authResult = try {
                firebaseAuth.signInWithEmailAndPassword(
                    loginCred.emailId,
                    loginCred.password
                ).await()
            } catch (e: Exception) {
                return Result.failure(e)
            }

            val userId = authResult.user?.uid ?: return Result.failure(
                Exception("Authentication succeeded but user ID is null")
            )

            // Add timeout for database operation
            val user:Users? = withTimeoutOrNull(5000) {
                getUserData(userId)
            } ?: return Result.failure(Exception("Database operation timed out"))

            user?.let {
                Result.success(it)
            }?: Result.failure(Exception("User data not found in database"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e("Login", "Invalid credentials: ${e.message}")
            Result.failure(e)
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e("Login", "Invalid user: ${e.message}")
            Result.failure(e)
        } catch (e: FirebaseNetworkException) {
            Log.e("Login", "Network error: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("Login", "Login failed with exception: ${e.message}")
            Result.failure(e)
        }


    }

    override suspend fun getUserData(userId: String): Users? {
        return try {
            val snapshot = firebaseDataBase.child(userId).get().await()
            if (snapshot.exists()) {
                snapshot.getValue(UsersDTO::class.java)?.toUsers()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Login", "Error getting user data", e)
            null
        }
    }


}