package com.wardabbass.redit

import android.app.Application
import com.androidnetworking.AndroidNetworking

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(this)
    }
}