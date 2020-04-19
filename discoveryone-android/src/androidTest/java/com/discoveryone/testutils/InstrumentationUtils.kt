package com.discoveryone.testutils

import androidx.test.platform.app.InstrumentationRegistry

fun waitForIdleSync() {
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
}