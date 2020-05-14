package com.discoveryone.initialization

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentActivity
import com.discoveryone.exceptions.NoActivityOnStack
import com.discoveryone.navigation.result.ResultRegistry
import com.discoveryone.utils.DiscoveryOneLog

internal object ActivityInterceptor {

    private val activities: MutableSet<FragmentActivity> = LinkedHashSet()

    fun register(application: Application) {
        application.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    if (activity !is FragmentActivity) return
                    activities.add(activity)
                }

                override fun onActivityDestroyed(activity: Activity) {
                    activities.remove(activity)
                    ResultRegistry.unregisterActivityResultLauncher(activity)
                }

                override fun onActivityStopped(activity: Activity) {
                    activities.remove(activity)
                }

                override fun onActivityStarted(activity: Activity) {
                    if (activity !is FragmentActivity) return
                    activities.add(activity)
                    logLastActivity()
                }

                override fun onActivityPaused(activity: Activity) = Unit
                override fun onActivityResumed(activity: Activity) = Unit
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) =
                    Unit
            })
    }

    private fun logLastActivity() {
        Log.i(DiscoveryOneLog.DISCOVERY_ONE_LOG_TAG, "current activity: ${getLast()}")
    }

    fun getLast(): FragmentActivity =
        activities.lastOrNull() ?: throw NoActivityOnStack()

    fun existsAnyActivity(): Boolean =
        activities.isNotEmpty()

    @VisibleForTesting
    fun getActivityByName(name: String): FragmentActivity =
        activities.first { it::class.simpleName == name }

    @VisibleForTesting
    fun clear() {
        activities.clear()
    }

    fun getActivityByHashCode(hashCode: Int): FragmentActivity =
        activities.first { it.hashCode() == hashCode }
}