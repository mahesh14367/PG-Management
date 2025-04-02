package com.appsv.loginapp.login.presentation.signup_screen.component

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log

// Function to get file name from URI
fun getFileNameFromUri(context: Context, uri: Uri): String {
    return try {
        var fileName = "Unknown File"
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex)
                }
            }
        }
        fileName
    } catch (e: Exception) {
        Log.e("FileUtils", "Error getting file name", e)
        "Unknown File"
    }
}