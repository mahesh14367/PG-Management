package com.appsv.loginapp.login.domain.utils

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object CloudinaryHelper {
    fun init(context: Context) {
        val config = hashMapOf(
            "cloud_name" to "drdd2j9br",
            "api_key" to "891381583945394",
            "api_secret" to "sY6lzxMxtKsB1pKItRU15fKUt3k", // Remove in production!
            "secure" to true
        )
        MediaManager.init(context, config)
    }

    suspend fun uploadImage(context: Context, uri: Uri): String = suspendCancellableCoroutine { continuation ->
        try {
            MediaManager.get().upload(uri) // Directly use URI here
                .unsigned("user_uploads") // Replace with your preset
                .callback(object : UploadCallback {
                    override fun onSuccess(requestId: String, resultData: Map<Any?, Any?>) {
                        val secureUrl = resultData["secure_url"]?.toString()
                            ?: run {
                                continuation.resumeWithException(Exception("No URL returned"))
                                return
                            }
                        continuation.resume(secureUrl)
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        continuation.resumeWithException(Exception(error.description))
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        continuation.resumeWithException(Exception("Upload failed: ${error.description}"))
                    }
                    override fun onStart(requestId: String) {}
                })
                .dispatch()
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }
}