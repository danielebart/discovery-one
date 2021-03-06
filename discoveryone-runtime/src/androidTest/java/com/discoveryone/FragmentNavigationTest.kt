package com.discoveryone

import com.discoveryone.extensions.navigator
import com.discoveryone.navigation.result.ResultRegistry.injectActivityResultSpy
import com.discoveryone.routes.GeneratedFragmentRoute
import com.discoveryone.testutils.ContainerTestActivity
import com.discoveryone.testutils.ListenForStringResultFromActivityTestFragment
import com.discoveryone.testutils.ListenForStringResultFromActivityTestFragmentRoute
import com.discoveryone.testutils.ListenForStringResultFromTestActivity
import com.discoveryone.testutils.ListenForStringResultTestButReceiverWrongResultTypFragment
import com.discoveryone.testutils.ListenForStringResultTestButReceiverWrongResultTypFragmentRoute
import com.discoveryone.testutils.ListenForStringResultTestFragment
import com.discoveryone.testutils.ListenForStringResultTestFragmentRoute
import com.discoveryone.testutils.ReturnIntValueTestFragment
import com.discoveryone.testutils.ReturnStringValueTestFragment
import com.discoveryone.testutils.ReturningValueSequence1TestFragment
import com.discoveryone.testutils.ReturningValueSequence1TestFragmentRoute
import com.discoveryone.testutils.ReturningValueSequence2TestFragment
import com.discoveryone.testutils.ReturningValueSequence3TestFragment
import com.discoveryone.testutils.TestFragment
import com.discoveryone.testutils.TestFragment2
import com.discoveryone.testutils.TestFragment2Route
import com.discoveryone.testutils.TestFragmentRoute
import com.discoveryone.testutils.TestResultSpy
import com.discoveryone.testutils.getFragment
import com.discoveryone.testutils.getSpecificFragment
import com.discoveryone.testutils.launchActivity
import com.discoveryone.testutils.waitForActivity
import com.discoveryone.testutils.waitForIdleSync
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.reflect.KClass

class FragmentNavigationTest {

    @Test
    fun givenAFragmentRouteWithoutArgs_whenNavigatingToThatFragment_thenCurrentTopFragmentShouldBeThatFragment() {
        val activity = launchActivity<ContainerTestActivity>()

        activity.navigator.navigate(FakeFragmentRouteWithoutArgs())
        waitForIdleSync()

        assertEquals(TestFragment::class, activity.getFragment()::class)
    }

    @Test
    fun givenAFragmentRouteWithStringAndDoubleArgs_whenNavigatingToThatFragment_thenCurrentTopFragmentShouldBeThatFragmentWithThoseArgs() {
        val fakeFragmentRoute = FakeFragmentRouteWithArgs(
            arg1 = "arg1_value",
            arg2 = 56789.0
        )
        val activity = launchActivity<ContainerTestActivity>()

        activity.navigator.navigate(fakeFragmentRoute)
        waitForIdleSync()

        val currentFragmentArgs = activity.getFragment().arguments!!
        assertEquals(TestFragment::class, activity.getFragment()::class)
        assertEquals("arg1_value", currentFragmentArgs.getString("arg1"))
        assertEquals(56789.0, currentFragmentArgs.getDouble("arg2"), 0.0)
    }

    @Test
    fun givenAFragmentListeningForAStringResult_whenNavigatingToANewFragmentWhichFinishesWithResult_thenVerifyRecordedResultsIsEqualsToExpectedResult() {
        val resultSpy = TestResultSpy()
        injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.navigator.navigate(ListenForStringResultTestFragmentRoute)
        waitForIdleSync()
        val expectedResult = "expected-result"

        activity.getSpecificFragment<ListenForStringResultTestFragment>()
            .navigateToFragmentReturningResult(expectedResult)
        waitForIdleSync()
        activity.getSpecificFragment<ReturnStringValueTestFragment>().returnResult()
        waitForIdleSync()

        assertEquals(listOf(expectedResult), resultSpy.getRecorderResults())
    }

    @Test
    fun givenAFragmentListeningForAStringResult_whenNavigatingToANewFragmentWhichFinishesWithDifferentResultType_thenVerifyRecordedResultsIsEmpty() {
        val resultSpy = TestResultSpy()
        injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.navigator.navigate(
            ListenForStringResultTestButReceiverWrongResultTypFragmentRoute
        )
        waitForIdleSync()

        activity.getSpecificFragment<ListenForStringResultTestButReceiverWrongResultTypFragment>()
            .navigateToFragmentReturningWrongResultType()
        waitForIdleSync()
        activity.getSpecificFragment<ReturnIntValueTestFragment>().returnResult()
        waitForIdleSync()

        assertEquals(emptyList<Any>(), resultSpy.getRecorderResults())
    }

    @Test
    fun givenASequenceOfFragmentsListeningAndReturningAStringValue_whenLaunchingFirstFragment_thenVerifyThatReturnedValuesAreCorrect() {
        val resultSpy = TestResultSpy()
        injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.navigator.navigate(ReturningValueSequence1TestFragmentRoute)
        waitForIdleSync()

        activity.getSpecificFragment<ReturningValueSequence1TestFragment>()
            .navigateToFragment2()
        waitForIdleSync()
        activity.getSpecificFragment<ReturningValueSequence2TestFragment>()
            .navigateToFragment3()
        waitForIdleSync()
        activity.getSpecificFragment<ReturningValueSequence3TestFragment>()
            .returnResult()
        waitForIdleSync()

        assertEquals(
            listOf("arg_from_fragment_3", "arg_from_fragment_2"),
            resultSpy.getRecorderResults()
        )
    }

    @Test
    fun whenNavigatingToTwoNewRoutesAndClosingTheLastOne_thenCurrentTopFragmentShouldBeTheFirstOne() {
        val activity = launchActivity<ContainerTestActivity>()
        activity.navigator.navigate(TestFragmentRoute)

        activity.navigator.navigate(TestFragment2Route)
        waitForIdleSync()
        activity.getSpecificFragment<TestFragment2>().navigator.close()
        waitForIdleSync()

        assertEquals(TestFragment::class, activity.getFragment()::class)
        assertEquals(1, activity.supportFragmentManager.fragments.size)
    }

    @Test
    fun givenAFragmentListeningForAStringResult_whenNavigatingToANewActivityWhichFinishesWithResult_thenVerifyRecordedResultIsEqualsToExpectedResult() {
        val resultSpy = TestResultSpy()
        injectActivityResultSpy(resultSpy)
        val expectedResult = "fake-result"
        val activity1 = launchActivity<ContainerTestActivity>()
        activity1.navigator.navigate(ListenForStringResultFromActivityTestFragmentRoute)
        waitForIdleSync()
        val fragment =
            activity1.getSpecificFragment<ListenForStringResultFromActivityTestFragment>()

        fragment.navigateToActivityReturningResult(expectedResult)
        waitForActivity()

        assertEquals(listOf(expectedResult), resultSpy.getRecorderResults())
    }

    @Test
    fun givenAnActivityListeningForAStringResult_whenNavigatingToANewFragmentWhichFinishesWithResult_thenVerifyRecordedResultsIsEqualsToExpectedResult() {
        val resultSpy = TestResultSpy()
        injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ListenForStringResultFromTestActivity>()
        val expectedResult = "expected-result"

        activity.navigateToFragmentReturningResult(expectedResult)
        waitForIdleSync()
        activity.getSpecificFragment<ReturnStringValueTestFragment>().returnResult()
        waitForIdleSync()

        assertEquals(listOf(expectedResult), resultSpy.getRecorderResults())
    }

    data class FakeFragmentRouteWithoutArgs(
        override val clazz: KClass<*> = TestFragment::class,
        override val containerId: Int = com.discoveryone.test.R.id.container
    ) : GeneratedFragmentRoute

    data class FakeFragmentRouteWithArgs(
        override val clazz: KClass<*> = TestFragment::class,
        override val containerId: Int = com.discoveryone.test.R.id.container,
        val arg1: String,
        val arg2: Double
    ) : GeneratedFragmentRoute
}