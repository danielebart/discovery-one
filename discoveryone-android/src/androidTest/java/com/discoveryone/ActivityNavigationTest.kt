package com.discoveryone

import androidx.fragment.app.FragmentActivity
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.BundleMatchers.hasEntry
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtras
import com.discoveryone.extensions.navigator
import com.discoveryone.initialization.ActivityInterceptor
import com.discoveryone.navigation.result.ActionLauncher
import com.discoveryone.routes.GeneratedActivityRoute
import com.discoveryone.testutils.ContainerTestActivity
import com.discoveryone.testutils.EmptyBundleMatcher
import com.discoveryone.testutils.EmptyTestActivity
import com.discoveryone.testutils.EmptyTestActivityRoute
import com.discoveryone.testutils.ListenForStringResultTestActivity
import com.discoveryone.testutils.ListenForStringResultTestButReceiverWrongResultTypeActivity
import com.discoveryone.testutils.ReturningValueSequence1TestActivity
import com.discoveryone.testutils.ReturningValueSequence2TestActivity
import com.discoveryone.testutils.TestResultSpy
import com.discoveryone.testutils.launchActivity
import com.discoveryone.testutils.recreateAndWait
import com.discoveryone.testutils.waitForActivity
import com.discoveryone.testutils.waitForIdleSync
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass

class ActivityNavigationTest {

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun teardown() {
        Intents.release()
        ActivityInterceptor.clear()
    }

    @Test
    fun givenAnActivityRouteWithoutArguments_whenNavigatingToThatActivity_thenInterceptedIntentContainsTheActivityCmpName() {
        val fakeActivityRoute = FakeActivityRouteWithoutArgs()
        val activity = launchActivity<ContainerTestActivity>()

        activity.navigator.navigate(fakeActivityRoute)

        intended(
            CoreMatchers.allOf(
                hasComponent(EmptyTestActivity::class.qualifiedName),
                hasExtras(EmptyBundleMatcher())
            )
        )
    }

    @Test
    fun givenAnActivityRouteWithAStringAndADoubleArgs_whenNavigatingToThatActivity_thenInterceptedIntentContainsTheActivityCmpNameAndThoseArgs() {
        val fakeActivityRoute = FakeActivityRouteWithArgs(
            arg1 = "arg1_value",
            arg2 = 56789.0
        )
        val activity = launchActivity<ContainerTestActivity>()

        activity.navigator.navigate(fakeActivityRoute)

        intended(
            allOf(
                hasComponent(EmptyTestActivity::class.qualifiedName),
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
    fun givenAnActivityListeningForAStringResult_whenNavigatingToANewActivityWhichFinishesWithResult_thenVerifyRecordedResultsIsEqualsToExpectedResult() {
        val resultSpy = TestResultSpy()
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ListenForStringResultTestActivity>()
        val expectedResult = "expected-result"

        activity.navigateToActivityReturningResult(expectedResult)
        waitForActivity()

        assertEquals(listOf(expectedResult), resultSpy.getRecorderResults())
    }

    @Test
    fun givenAnActivityListeningForAStringResult_whenNavigatingToANewActivityWhichFinishesWithDifferentResultType_thenVerifyRecordedResultsIsEmpty() {
        val resultSpy = TestResultSpy()
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ListenForStringResultTestButReceiverWrongResultTypeActivity>()

        activity.navigateToActivityReturningWrongResultType()
        waitForIdleSync()

        assertEquals(emptyList<Any>(), resultSpy.getRecorderResults())
    }

    @Test
    fun givenASequenceOfActivitiesListeningAndReturningAStringValue_whenLaunchingFirstActivity_thenVerifyThatReturnedValuesAreCorrect() {
        val resultSpy = TestResultSpy()
        ActionLauncher.injectActivityResultSpy(resultSpy)

        launchActivity<ReturningValueSequence1TestActivity>().navigateToActivity2()
        waitForActivity()
        getActivity<ReturningValueSequence2TestActivity>().navigateToActivity3()
        waitForActivity()

        assertEquals(
            listOf("arg_from_activity_3", "arg_from_activity_2"),
            resultSpy.getRecorderResults()
        )
    }

    @Test
    fun givenASequenceOfActivitiesListeningAndReturningAStringValue_whenLaunchingFirstActivityAndRecreating_thenVerifyThatReturnedValuesAreCorrect() {
        val resultSpy = TestResultSpy()
        ActionLauncher.injectActivityResultSpy(resultSpy)

        val activity1 = launchActivity<ReturningValueSequence1TestActivity>()
        activity1.navigateToActivity2()
        waitForActivity()
        val activity2 = getActivity<ReturningValueSequence2TestActivity>()
        activity1.recreateAndWait()
        activity2.navigateToActivity3()
        waitForActivity()

        assertEquals(
            listOf("arg_from_activity_3", "arg_from_activity_2"),
            resultSpy.getRecorderResults()
        )
    }

    @Test
    fun whenNavigatingToTwoNewRoutesAndClosingTheLastOne_thenSecondActivityShouldBeInFinishingStateAndFirstNot() {
        val activity1 = launchActivity<ContainerTestActivity>()

        activity1.navigator.navigate(EmptyTestActivityRoute)
        waitForActivity()
        val activity2 = ActivityInterceptor.getLast()
        activity2.navigator.close()
        waitForActivity()

        assertTrue(activity2.isFinishing)
        assertFalse(activity1.isFinishing)
    }


    private inline fun <reified T : FragmentActivity> getActivity(): T =
        ActivityInterceptor.getActivityByName(T::class.simpleName.toString()) as T

    data class FakeActivityRouteWithoutArgs(
        override val clazz: KClass<*> = EmptyTestActivity::class
    ) : GeneratedActivityRoute

    data class FakeActivityRouteWithArgs(
        override val clazz: KClass<*> = EmptyTestActivity::class,
        val arg1: String,
        val arg2: Double
    ) : GeneratedActivityRoute
}