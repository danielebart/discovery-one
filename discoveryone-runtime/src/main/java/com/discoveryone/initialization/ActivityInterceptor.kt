package com.discoveryone.initialization

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.discoveryone.navigation.result.ResultRegistry

internal object ActivityInterceptor {

    fun register(application: Application) {
        application.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityDestroyed(activity: Activity) {
                    // the activity is destroyed: we don't need anymore the saved ActivityResultLauncher
                    ResultRegistry.unregisterActivityResultLauncher(activity)
                }

                override fun onActivityStopped(activity: Activity) = Unit
                override fun onActivityStarted(activity: Activity) = Unit
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) =
                    Unit

                override fun onActivityPaused(activity: Activity) = Unit
                override fun onActivityResumed(activity: Activity) = Unit
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) =
                    Unit
            })
    }
}