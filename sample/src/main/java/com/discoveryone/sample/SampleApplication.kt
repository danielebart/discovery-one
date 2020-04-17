package com.discoveryone.sample

import android.app.Application
import com.discoveryone.NavigatorActivityLifecycleCallback

class SampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(NavigatorActivityLifecycleCallback)
    }
}