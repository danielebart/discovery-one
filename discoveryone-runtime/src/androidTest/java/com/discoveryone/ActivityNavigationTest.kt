package com.discoveryone

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.BundleMatchers.hasEntry
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtras
import com.discoveryone.extensions.navigator
import com.discoveryone.navigation.result.ResultRegistry.injectActivityResultSpy
import com.discoveryone.routes.GeneratedActivityRoute
import com.discoveryone.testutils.ContainerTestActivity
import com.discoveryone.testutils.EmptyTestActivity
import com.discoveryone.testutils.ListenForStringResultTestActivity
import com.discoveryone.testutils.ListenForStringResultTestButReceiverWrongResultTypeActivity
import com.discoveryone.testutils.TestResultSpy
import com.discoveryone.testutils.launchActivity
import com.discoveryone.testutils.waitForActivity
import com.discoveryone.testutils.waitForIdleSync
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
    fun givenAnActivityRouteWithoutArguments_whenNavigatingToThatActivity_thenInterceptedIntentContainsTheActivityCmpName() {
        val fakeActivityRoute = FakeActivityRouteWithoutArgs()
        val activity = launchActivity<ContainerTestActivity>()

        activity.navigator.navigate(fakeActivityRoute)

        intended(hasComponent(EmptyTestActivity::class.qualifiedName))
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
        injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ListenForStringResultTestActivity>()
        val expectedResult = "expected-result"

        activity.navigateToActivityReturningResult(expectedResult)
        waitForActivity()

        assertEquals(listOf(expectedResult), resultSpy.getRecorderResults())
    }

    @Test
    fun givenAnActivityListeningForAStringResult_whenNavigatingToANewActivityWhichFinishesWithDifferentResultType_thenVerifyRecordedResultsIsEmpty() {
        val resultSpy = TestResultSpy()
        injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ListenForStringResultTestButReceiverWrongResultTypeActivity>()

        activity.navigateToActivityReturningWrongResultType()
        waitForIdleSync()

        assertEquals(emptyList<Any>(), resultSpy.getRecorderResults())
    }

    data class FakeActivityRouteWithoutArgs(
        override val clazz: KClass<*> = EmptyTestActivity::class
    ) : GeneratedActivityRoute

    data class FakeActivityRouteWithArgs(
        override val clazz: KClass<*> = EmptyTestActivity::class,
        val arg1: String,
        val arg2: Double
    ) : GeneratedActivityRoute
}