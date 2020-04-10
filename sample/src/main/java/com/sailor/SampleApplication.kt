package com.sailor

import android.app.Application

class SampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(NavigatorActivityLifecycleCallback())
    }
}