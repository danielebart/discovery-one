package com.discoveryone.initialization

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentActivity
import com.discoveryone.exceptions.NoActivityOnStack
import com.discoveryone.navigation.ActivityNavigation
import java.util.Deque
import java.util.concurrent.LinkedBlockingDeque

internal object ActivityInterceptor {

    private val activities: Deque<FragmentActivity> = LinkedBlockingDeque()

    fun register(application: Application) {
        application.registerActivityLifecycleCallbacks(object : ActivityCreationLifecycleCallback {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                (activity as? FragmentActivity)?.let {
                    activities.push(activity)
                }
            }

            override fun onActivityDestroyed(activity: Activity) {
                activities.remove(activity)
                ActivityNavigation.unregisterActivityResultLauncher(activity)
            }
        })
    }

    fun getLast(): FragmentActivity =
        activities.peek() ?: throw NoActivityOnStack()

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