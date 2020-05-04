package com.discoveryone.testutils

import android.app.Activity
import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry

inline fun <reified T : Activity> launchActivity(): T {
    lateinit var activity: Activity
    ActivityScenario.launch(T::class.java).onActivity { launchedActivity ->
        activity = launchedActivity
    }
    return activity as T
}

fun onMainThread(action: () -> Unit) {
    InstrumentationRegistry.getInstrumentation().runOnMainSync { action() }
    waitForIdleSync()
}

fun Activity.recreateAndWait() {
    onMainThread { recreate() }
    waitForActivity()
}

fun waitForIdleSync() {
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
}

fun waitForActivity() {
    waitForIdleSync()
    SystemClock.sleep(700) // Fixes flaky tests on activity creation
    waitForIdleSync()
}