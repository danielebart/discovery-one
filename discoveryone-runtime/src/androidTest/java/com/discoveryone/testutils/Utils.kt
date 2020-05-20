package com.discoveryone.testutils

import android.app.Activity
import android.content.Intent
import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import java.util.UUID

inline fun <reified T : Activity> launchActivity(withActivityTag: Boolean = true): T {
    lateinit var activity: Activity
    val intent = Intent(
        ApplicationProvider.getApplicationContext(),
        T::class.java
    )
    if (withActivityTag) {
        intent.putExtra("ACTIVITY_TAG_KEY", UUID.randomUUID().toString())
    }
    ActivityScenario.launch<T>(intent).onActivity { launchedActivity ->
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