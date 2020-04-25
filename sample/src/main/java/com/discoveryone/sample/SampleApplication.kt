package com.discoveryone.sample

import android.app.Application
import com.discoveryone.AndroidNavigator
import com.discoveryone.Navigator

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Navigator.initialize(AndroidNavigator(this))
    }
}