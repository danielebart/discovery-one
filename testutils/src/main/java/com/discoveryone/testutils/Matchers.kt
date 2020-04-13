package com.discoveryone.testutils

import android.os.Bundle
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class EmptyBundleMatcher : TypeSafeMatcher<Bundle>() {

    override fun describeTo(description: Description) {
        description.appendText("bundle is empty")
    }

    override fun matchesSafely(bundle: Bundle): Boolean =
        bundle.size() == 0
}