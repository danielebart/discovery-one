package com.discoveryone

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.BundleMatchers.hasEntry
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtras
import com.discoveryone.destination.ActivityDestination
import com.discoveryone.destination.FragmentDestination
import com.discoveryone.destination.InternalDestinationArgumentMarker
import com.discoveryone.testutils.EmptyBundleMatcher
import com.discoveryone.testutils.TestActivity2
import com.discoveryone.testutils.TestFragment
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Stack
import kotlin.reflect.KClass

@RunWith(RobolectricTestRunner::class)
class AndroidNavigationHandlerTest {

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun teardown() {
        Intents.release()
    }

    @Test
    fun `GIVEN an activity destination without arguments WHEN navigating to that activity THEN intercepted intent contains the activity cmp name`() {
        val fakeActivityDestination = FakeActivityDestinationWithoutArgs()
        val androidNavigationHandler = createAndroidNavigationHandler(launchActivity())

        androidNavigationHandler.navigate(fakeActivityDestination)

        intended(
            allOf(
                hasComponent(TestActivity2::class.qualifiedName),
                hasExtras(EmptyBundleMatcher())
            )
        )
    }

    @Test
    fun `GIVEN an activity destination with a string and a double arguments WHEN navigating to that activity THEN intercepted intent contains the activity cmp nam and those args`() {
        val fakeActivityDestination =
            FakeActivityDestinationWithArgs(arg1 = "arg1_value", arg2 = 56789.0)
        val androidNavigationHandler = createAndroidNavigationHandler(launchActivity())

        androidNavigationHandler.navigate(fakeActivityDestination)

        intended(
            allOf(
                hasComponent(TestActivity2::class.qualifiedName),
                hasExtras(
                    allOf(
                        hasEntry("arg1", "arg1_value"),
                        hasEntry("arg2", 56789.0)
                    )
                )
            )
        )
    }

    @Test
    fun `GIVEN a fragment destination without arguments WHEN navigating to that fragment THEN current top fragment should be that fragment with no args`() {
        val fakeFragmentDestination = FakeFragmentDestinationWithoutArgs()
        val activity = launchActivity()
        val androidNavigationHandler = createAndroidNavigationHandler(activity)

        androidNavigationHandler.navigate(fakeFragmentDestination)

        val currentFragment = activity.supportFragmentManager.fragments[0]
        assertEquals(TestFragment::class, currentFragment::class)
        assertEquals(0, currentFragment.arguments!!.size())
    }

    @Test
    fun `GIVEN a fragment destination with a string and a double arguments WHEN navigating to that fragment THEN current top fragment should be that fragment with those args`() {
        val fakeFragmentDestination =
            FakeFragmentDestinationWithArgs(arg1 = "arg1_value", arg2 = 56789.0)
        val activity = launchActivity()
        val androidNavigationHandler = createAndroidNavigationHandler(activity)

        androidNavigationHandler.navigate(fakeFragmentDestination)

        val currentFragment = activity.supportFragmentManager.fragments[0]
        val currentFragmentArgs = currentFragment.arguments!!
        assertEquals(TestFragment::class, currentFragment::class)
        assertEquals("arg1_value", currentFragmentArgs.getString("arg1"))
        assertEquals(56789.0, currentFragmentArgs.getDouble("arg2"))
    }

    private fun launchActivity(): AppCompatActivity {
        lateinit var activity: Activity
        ActivityScenario.launch(com.discoveryone.testutils.TestActivity1::class.java)
            .onActivity { launchedActivity ->
                activity = launchedActivity
            }
        return activity as AppCompatActivity
    }

    private fun createAndroidNavigationHandler(withActivity: AppCompatActivity): AndroidNavigationHandler {
        val stack = Stack<AppCompatActivity>()
        stack.push(withActivity)
        return AndroidNavigationHandler(stack)
    }

    data class FakeFragmentDestinationWithoutArgs(
        override val name: String = "fragment",
        override val clazz: KClass<*> = TestFragment::class,
        override val containerId: Int = com.discoveryone.testutils.R.id.container
    ) : FragmentDestination

    data class FakeFragmentDestinationWithArgs(
        override val name: String = "fragment",
        override val clazz: KClass<*> = TestFragment::class,
        override val containerId: Int = com.discoveryone.testutils.R.id.container,
        @InternalDestinationArgumentMarker val arg1: String,
        @InternalDestinationArgumentMarker val arg2: Double
    ) : FragmentDestination

    data class FakeActivityDestinationWithoutArgs(
        override val name: String = "activity",
        override val clazz: KClass<*> = TestActivity2::class
    ) : ActivityDestination

    data class FakeActivityDestinationWithArgs(
        override val name: String = "activity",
        override val clazz: KClass<*> = TestActivity2::class,
        @InternalDestinationArgumentMarker val arg1: String,
        @InternalDestinationArgumentMarker val arg2: Double
    ) : ActivityDestination
}