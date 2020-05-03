package com.discoveryone.initialization

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.discoveryone.navigation.ActivityNavigation

internal object NavigatorActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        (activity as? FragmentActivity)?.let {
            ActivityStackContainer.push(activity)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        ActivityStackContainer.remove(activity)
        ActivityNavigation.unregisterActivityResultLauncher(activity)
    }

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityStopped(activity: Activity) = Unit
}