package com.discoveryone

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import org.junit.runners.Suite

@Suite.SuiteClasses(ActivityNavigationTest::class)
class NavigationTest {

//
//
//

//
//


    private inline fun <reified T : Activity> launchActivity(): T {
        lateinit var activity: Activity
        ActivityScenario.launch(T::class.java).onActivity { launchedActivity ->
            activity = launchedActivity
        }
        return activity as T
    }

}
