package com.discoveryone

import androidx.fragment.app.FragmentActivity
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.BundleMatchers.hasEntry
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtras
import com.discoveryone.annotations.InternalDestinationArgumentMarker
import com.discoveryone.destinations.ActivityDestination
import com.discoveryone.extensions.scene
import com.discoveryone.initialization.ActivityStackContainer
import com.discoveryone.navigation.result.ActionLauncher
import com.discoveryone.testutils.ContainerTestActivity
import com.discoveryone.testutils.EmptyBundleMatcher
import com.discoveryone.testutils.EmptyTestActivity
import com.discoveryone.testutils.ListenForStringResultTestActivity
import com.discoveryone.testutils.ListenForStringResultTestButReceiverWrongResultTypeActivity
import com.discoveryone.testutils.ReturningValueSequence1TestActivity
import com.discoveryone.testutils.ReturningValueSequence2TestActivity
import com.discoveryone.testutils.TestResultSpy
import com.discoveryone.testutils.launchActivity
import com.discoveryone.testutils.onMainThread
import com.discoveryone.testutils.waitForIdleSync
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Assert.assertEquals
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
    }

    @Test
    fun givenAnActivityDestinationWithoutArguments_whenNavigatingToThatActivity_thenInterceptedIntentContainsTheActivityCmpName() {
        val fakeActivityDestination = FakeActivityDestinationWithoutArgs()
        val activity = launchActivity<ContainerTestActivity>()

        activity.scene.navigate(fakeActivityDestination)

        intended(
            CoreMatchers.allOf(
                hasComponent(EmptyTestActivity::class.qualifiedName),
                hasExtras(EmptyBundleMatcher())
            )
        )
    }

    @Test
    fun givenAnActivityDestinationWithAStringAndADoubleArgs_whenNavigatingToThatActivity_thenInterceptedIntentContainsTheActivityCmpNameAndThoseArgs() {
        val fakeActivityDestination = FakeActivityDestinationWithArgs(
            arg1 = "arg1_value",
            arg2 = 56789.0
        )
        val activity = launchActivity<ContainerTestActivity>()

        activity.scene.navigate(fakeActivityDestination)

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
        waitForIdleSync()

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
        getActivity<ReturningValueSequence2TestActivity>().navigateToActivity3()

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
        val activity2 = getActivity<ReturningValueSequence2TestActivity>()
        onMainThread { activity1.recreate() }
        activity2.navigateToActivity3()

        assertEquals(
            listOf("arg_from_activity_3", "arg_from_activity_2"),
            resultSpy.getRecorderResults()
        )
    }

    private inline fun <reified T : FragmentActivity> getActivity(): T =
        ActivityStackContainer.getByName(T::class.simpleName.toString()) as T

    data class FakeActivityDestinationWithoutArgs(
        override val clazz: KClass<*> = EmptyTestActivity::class
    ) : ActivityDestination

    data class FakeActivityDestinationWithArgs(
        override val clazz: KClass<*> = EmptyTestActivity::class,
        @InternalDestinationArgumentMarker val arg1: String,
        @InternalDestinationArgumentMarker val arg2: Double
    ) : ActivityDestination
}