package com.discoveryone.initialization

import android.app.Activity
import android.app.Application
import android.os.Bundle

internal interface ActivityCreationLifecycleCallback : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
}