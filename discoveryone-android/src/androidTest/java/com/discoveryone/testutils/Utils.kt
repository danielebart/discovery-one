package com.discoveryone.testutils

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry

inline fun <reified T : Activity> launchActivity(): T {
    lateinit var activity: Activity
    ActivityScenario.launch(T::class.java).onActivity { launchedActivity ->
        activity = launchedActivity
    }
    return activity as T
}

//inline fun <reified T : Activity> launchActivityReturningScenario(): Pair<T, ActivityScenario<T>> {
//    lateinit var activity: Activity
//    val scenario = ActivityScenario.launch(T::class.java).onActivity { launchedActivity ->
//        activity = launchedActivity
//    }
//    return Pair(activity as T, scenario)
//}

fun onMainThread(action: () -> Unit) {
    InstrumentationRegistry.getInstrumentation().runOnMainSync { action() }
    waitForIdleSync()
}