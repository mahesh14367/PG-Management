package com.appsv.loginapp

import android.app.Application
import com.appsv.loginapp.login.domain.utils.CloudinaryHelper
import com.cloudinary.Cloudinary

// App.kt
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CloudinaryHelper.init(this)
    }
}