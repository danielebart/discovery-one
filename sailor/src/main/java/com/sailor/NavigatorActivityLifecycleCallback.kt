package com.sailor

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sailor.Navigator

class NavigatorActivityLifecycleCallback: Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        Navigator.stack.remove(activity)
        Log.d("Navigator", "removing ${activity::class.simpleName}")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Navigator.stack.push(activity as AppCompatActivity)
        Log.d("Navigator", "pushing ${activity::class.simpleName}")
    }

    override fun onActivityResumed(activity: Activity) = Unit

}