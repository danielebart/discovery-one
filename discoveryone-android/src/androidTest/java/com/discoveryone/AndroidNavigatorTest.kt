package com.discoveryone

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.BundleMatchers.hasEntry
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtras
import com.discoveryone.annotations.InternalDestinationArgumentMarker
import com.discoveryone.destinations.ActivityDestination
import com.discoveryone.destinations.FragmentDestination
import com.discoveryone.result.ActivityResultLauncherFactory
import com.discoveryone.testutils.EmptyBundleMatcher
import com.discoveryone.testutils.TestActivity2
import com.discoveryone.testutils.TestActivityWithResultRegisterAfterOnCreated
import com.discoveryone.testutils.TestActivityWithResultRegisterBeforeOnCreated
import com.discoveryone.testutils.TestActivityWithWrongResultType
import com.discoveryone.testutils.TestContainerActivity
import com.discoveryone.testutils.TestFragment
import com.discoveryone.testutils.TestResultSpy
import com.discoveryone.testutils.TestReturningValueSequence1Activity
import com.discoveryone.testutils.waitForIdleSync
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass

class AndroidNavigatorTest {

    @Before
    fun setup() {
        Navigator.initialize(
            AndroidNavigator(
                ApplicationProvider.getApplicationContext()
            )
        )
        Intents.init()
    }

    @After
    fun teardown() {
        Intents.release()
    }

    @Test
    fun givenAnActivityDestinationWithoutArguments_whenNavigatingToThatActivity_thenInterceptedIntentContainsTheActivityCmpName() {
        val fakeActivityDestination =
            FakeActivityDestinationWithoutArgs()
        launchActivity<TestContainerActivity>()

        Navigator.navigate(fakeActivityDestination)

        intended(
            allOf(
                hasComponent(TestActivity2::class.qualifiedName),
                hasExtras(EmptyBundleMatcher())
            )
        )
    }

    @Test
    fun givenAnActivityDestinationWithAStringAndADoubleArgs_whenNavigatingToThatActivity_thenInterceptedIntentContainsTheActivityCmpNameAndThoseArgs() {
        val fakeActivityDestination =
            FakeActivityDestinationWithArgs(
                arg1 = "arg1_value",
                arg2 = 56789.0
            )
        launchActivity<TestContainerActivity>()

        Navigator.navigate(fakeActivityDestination)

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
    fun givenAnActivityRegisterStringResultBeforeOnCreated_whenNavigatingToANewActivityWhichFinishesWithResult_thenVerifyRecordedResultsIsEqualsToExpectedResult() {
        val resultSpy = TestResultSpy()
        ActivityResultLauncherFactory.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<TestActivityWithResultRegisterBeforeOnCreated>()
        val expectedResult = "expected-result"

        // activity is navigating for result to TestReturningValueActivity
        activity.navigateToActivityReturningResult(expectedResult)
        waitForIdleSync()

        assertEquals(listOf(expectedResult), resultSpy.getRecorderResults())
    }

    @Test
    fun givenAnActivityRegisterStringResultAfterOnCreated_whenNavigatingToANewActivityWhichFinishesWithResult_thenVerifyRecordedResultsIsEqualsToExpectedResult() {
        val resultSpy = TestResultSpy()
        ActivityResultLauncherFactory.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<TestActivityWithResultRegisterAfterOnCreated>()
        val expectedResult = "expected-result"

        // activity is navigating for result to TestReturningValueActivity
        activity.navigateToActivityReturningResult(expectedResult)
        waitForIdleSync()

        assertEquals(listOf(expectedResult), resultSpy.getRecorderResults())
    }

    @Test
    fun givenAnActivityRegisterStringResult_whenNavigatingToANewActivityWhichFinishesWithDifferentResultType_thenVerifyRecordedResultsIsEmpty() {
        val resultSpy = TestResultSpy()
        ActivityResultLauncherFactory.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<TestActivityWithWrongResultType>()

        // activity is navigating for result to an activity which returns an unexpected result type
        activity.navigateToActivityReturningResult()
        waitForIdleSync()

        assertEquals(emptyList<Any>(), resultSpy.getRecorderResults())
    }

    @Test
    fun givenASequenceOfActivitiesWhichRegisterAndReturnAStringValue_whenLaunchingFirstActivity_thenVerifyThatReturnedValuesAreCorrect() {
        val resultSpy = TestResultSpy()
        ActivityResultLauncherFactory.injectActivityResultSpy(resultSpy)

        // TestReturningValueSequence1Activity is registering a String result
        // and navigates to TestReturningValueSequence2Activity which in turns registers
        // a String result from TestReturningValueSequence3Activity.
        launchActivity<TestReturningValueSequence1Activity>()
        waitForIdleSync()

        assertEquals(
            listOf("arg_from_activity_3", "arg_from_activity_2"),
            resultSpy.getRecorderResults()
        )
    }

    @Test
    fun givenAFragmentDestinationWithoutArgs_whenNavigatingToThatFragment_thenCurrentTopFragmentShouldBeThatFragmentWithNoArgs() {
        val fakeFragmentDestination =
            FakeFragmentDestinationWithoutArgs()
        val activity = launchActivity<TestContainerActivity>()

        Navigator.navigate(fakeFragmentDestination)
        waitForIdleSync()

        val currentFragment = activity.supportFragmentManager.fragments[0]
        assertEquals(TestFragment::class, currentFragment::class)
        assertEquals(0, currentFragment.arguments!!.size())
    }

    @Test
    fun givenAFragmentDestinationWithStringAndDoubleArgs_whenNavigatingToThatFragment_thenCurrentTopFragmentShouldBeThatFragmentWithThoseArgs() {
        val fakeFragmentDestination =
            FakeFragmentDestinationWithArgs(
                arg1 = "arg1_value",
                arg2 = 56789.0
            )
        val activity = launchActivity<TestContainerActivity>()

        Navigator.navigate(fakeFragmentDestination)
        waitForIdleSync()

        val currentFragment = activity.supportFragmentManager.fragments[0]
        val currentFragmentArgs = currentFragment.arguments!!
        assertEquals(TestFragment::class, currentFragment::class)
        assertEquals("arg1_value", currentFragmentArgs.getString("arg1"))
        assertEquals(56789.0, currentFragmentArgs.getDouble("arg2"), 0.0)
    }

    private inline fun <reified T : Activity> launchActivity(): T {
        lateinit var activity: Activity
        ActivityScenario.launch(T::class.java).onActivity { launchedActivity ->
            activity = launchedActivity
        }
        return activity as T
    }

    data class FakeFragmentDestinationWithoutArgs(
        override val clazz: KClass<*> = TestFragment::class,
        override val containerId: Int = com.discoveryone.test.R.id.container
    ) : FragmentDestination

    data class FakeFragmentDestinationWithArgs(
        override val clazz: KClass<*> = TestFragment::class,
        override val containerId: Int = com.discoveryone.test.R.id.container,
        @InternalDestinationArgumentMarker val arg1: String,
        @InternalDestinationArgumentMarker val arg2: Double
    ) : FragmentDestination

    data class FakeActivityDestinationWithoutArgs(
        override val clazz: KClass<*> = TestActivity2::class
    ) : ActivityDestination

    data class FakeActivityDestinationWithArgs(
        override val clazz: KClass<*> = TestActivity2::class,
        @InternalDestinationArgumentMarker val arg1: String,
        @InternalDestinationArgumentMarker val arg2: Double
    ) : ActivityDestination
}
