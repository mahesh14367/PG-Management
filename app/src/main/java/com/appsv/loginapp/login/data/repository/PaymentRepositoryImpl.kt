package com.appsv.loginapp.login.data.repository

import android.util.Log
import com.appsv.loginapp.login.domain.model.Transaction
import com.appsv.loginapp.login.domain.repository.PaymentRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class PaymentRepositoryImpl:PaymentRepository {

    private val auth=FirebaseAuth.getInstance()

    private val db = FirebaseDatabase.getInstance().getReference("Transactions").child("${auth.currentUser?.uid}")

    override suspend fun saveTransaction(transaction: Transaction): Result<Unit> {
        return try {
            db.child(transaction.id).setValue(transaction).await()
            Log.d("transaction","saved")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("transaction","failed")
            Result.failure(e)
        }
    }

}