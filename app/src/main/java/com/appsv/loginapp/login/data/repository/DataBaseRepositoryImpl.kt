package com.appsv.loginapp.login.data.repository

import android.util.Log
import com.appsv.loginapp.core.data.GuestDTO
import com.appsv.loginapp.core.data.StaffDTO
import com.appsv.loginapp.core.data.UsersDTO
import com.appsv.loginapp.core.domain_mapper.toGuest
import com.appsv.loginapp.core.domain_mapper.toStaff
import com.appsv.loginapp.core.domain_mapper.toUsers
import com.appsv.loginapp.login.data.toGuestDTO
import com.appsv.loginapp.login.data.toStaffDTO
import com.appsv.loginapp.login.domain.model.Guest
import com.appsv.loginapp.login.domain.model.Staff
import com.appsv.loginapp.login.domain.model.Users
import com.appsv.loginapp.login.domain.repository.DataBaseRepository
import com.appsv.loginapp.login.domain.utils.Role
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull


class DataBaseRepositoryImpl:DataBaseRepository {

    private val auth =FirebaseAuth.getInstance()
    private val firebaseDataBase = FirebaseDatabase.getInstance().getReference("Users")

    override suspend fun getAllUsers(): Flow<List<Users>?> = callbackFlow {
        try {
            val user: String? = withTimeoutOrNull(10000) {
                auth.currentUser?.uid?.let { getUserData(it) }
            }
            Log.d("role", "$user")

            if (!user.isNullOrEmpty() && (user == Role.ADMIN.name || user == Role.OWNER.name)) {
                Log.d("If", "inside if block")

                val listener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val usersList: List<Users> = snapshot.children.mapNotNull {
                            it.getValue(UsersDTO::class.java)
                        }.map {
                            it.toUsers()
                        }
                        Log.d("DataBaseRepositoryImpl", "getAllUsers() result: $usersList")
                        trySend(usersList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("DataBaseRepositoryImpl", "getAllUsers() result: failed")
                        close()
                    }
                }
                firebaseDataBase.addValueEventListener(listener)
                awaitClose { firebaseDataBase.removeEventListener(listener) }
            } else {
                trySend(null)
                close()
            }
        } catch (e: Exception) {
            Log.e("DataBaseRepositoryImpl", "Error fetching users", e)
            trySend(null)
            close()
        }
    }



    override suspend fun updateUser(user: Users) {
        try {
            firebaseDataBase.child(user.userId!!).setValue(user).await()
        }catch (e:Exception){
            Log.e("Updating Guest","${e.message}")
        }
    }

    override suspend fun deleteUser(userId: String) {
        try{
            firebaseDataBase.child(userId).removeValue().await()
        }catch (e:Exception){
            Log.e("Deleting Guest","${e.message}")
        }
    }

     private suspend fun getUserData(userId: String): String? {
         return try {
             val snapshot = firebaseDataBase.child(userId).get().await()
             if (snapshot.exists()) {
                 snapshot.getValue(UsersDTO::class.java)?.toUsers()?.role
             } else {
                 null
             }
         } catch (e: Exception) {
             Log.e("Login", "Error getting user data", e)
             null
         }
    }

}