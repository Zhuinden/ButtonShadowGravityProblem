package com.acme.debugbuttonproject

import android.app.Application
import com.acme.debugbuttonproject.density

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        density = resources.displayMetrics.density
    }
}