package com.discoveryone

import androidx.fragment.app.FragmentActivity
import com.discoveryone.extensions.navigator
import com.discoveryone.initialization.ActivityStackContainer
import com.discoveryone.navigation.result.ActionLauncher
import com.discoveryone.routes.GeneratedDialogFragmentRoute
import com.discoveryone.testutils.ContainerTestActivity
import com.discoveryone.testutils.ListenForStringResulButReceiverWrongResultTypeFromDialogTestFragment
import com.discoveryone.testutils.ListenForStringResulButReceiverWrongResultTypeFromDialogTestFragmentRoute
import com.discoveryone.testutils.ListenForStringResultFromDialogTestActivity
import com.discoveryone.testutils.ListenForStringResultFromDialogTestFragment
import com.discoveryone.testutils.ListenForStringResultFromDialogTestFragmentRoute
import com.discoveryone.testutils.ListenForStringResultTestDialogFragment
import com.discoveryone.testutils.ListenForStringResultTestDialogFragmentRoute
import com.discoveryone.testutils.ReturnIntValueTestDialogFragment
import com.discoveryone.testutils.ReturnStringValueTestDialogFragment
import com.discoveryone.testutils.ReturningValueSequence1TestDialogFragment
import com.discoveryone.testutils.ReturningValueSequence1TestDialogFragmentRoute
import com.discoveryone.testutils.ReturningValueSequence3TestDialogFragment
import com.discoveryone.testutils.ReturningValueSequenceOfDialogsTestFragment
import com.discoveryone.testutils.TestDialogFragment
import com.discoveryone.testutils.TestDialogFragment2
import com.discoveryone.testutils.TestDialogFragment2Route
import com.discoveryone.testutils.TestDialogFragmentRoute
import com.discoveryone.testutils.TestResultSpy
import com.discoveryone.testutils.getFragment
import com.discoveryone.testutils.getSpecificFragment
import com.discoveryone.testutils.launchActivity
import com.discoveryone.testutils.recreateAndWait
import com.discoveryone.testutils.waitForIdleSync
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.reflect.KClass

class DialogFragmentNavigationTest {

    @Test
    fun givenADialogFragmentRouteWithoutArgs_whenNavigatingToThatDialogFragment_thenCurrentTopDialogFragmentShouldBeThatDialogFragment() {
        val activity = launchActivity<ContainerTestActivity>()

        activity.navigator.navigate(FakeDialogFragmentRouteWithoutArgs())
        waitForIdleSync()

        assertEquals(TestDialogFragment::class, activity.getFragment()::class)
    }

    @Test
    fun givenADialogFragmentRouteWithStringAndDoubleArgs_whenNavigatingToThatDialogFragment_thenCurrentTopDialogFragmentShouldBeThatDialogFragmentWithThoseArgs() {
        val fakeDialogFragmentRoute = FakeDialogFragmentRouteWithArgs(
            arg1 = "arg1_value",
            arg2 = 56789.0
        )
        val activity = launchActivity<ContainerTestActivity>()

        activity.navigator.navigate(fakeDialogFragmentRoute)
        waitForIdleSync()

        val currentDialogFragmentArgs = activity.getFragment().arguments!!
        assertEquals(TestDialogFragment::class, activity.getFragment()::class)
        assertEquals("arg1_value", currentDialogFragmentArgs.getString("arg1"))
        assertEquals(56789.0, currentDialogFragmentArgs.getDouble("arg2"), 0.0)
    }

    @Test
    fun givenAFragmentListeningForAStringResult_whenNavigatingToANewDialogFragmentWhichFinishesWithResult_thenVerifyRecordedResultsIsEqualsToExpectedResult() {
        val resultSpy = TestResultSpy()
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.navigator.navigate(ListenForStringResultFromDialogTestFragmentRoute)
        waitForIdleSync()
        val expectedResult = "expected-result"

        activity.getSpecificFragment<ListenForStringResultFromDialogTestFragment>()
            .navigateToDialogFragmentReturningResult(expectedResult)
        waitForIdleSync()
        activity.getSpecificFragment<ReturnStringValueTestDialogFragment>().returnResult()
        waitForIdleSync()

        assertEquals(listOf(expectedResult), resultSpy.getRecorderResults())
    }

    @Test
    fun givenAnActivityListeningForAStringResult_whenNavigatingToANewDialogFragmentWhichFinishesWithResult_thenVerifyRecordedResultsIsEqualsToExpectedResult() {
        val resultSpy = TestResultSpy()
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ListenForStringResultFromDialogTestActivity>()
        val expectedResult = "expected-result"

        activity.navigateToDialogFragmentReturningResult(expectedResult)
        waitForIdleSync()
        activity.getSpecificFragment<ReturnStringValueTestDialogFragment>().returnResult()
        waitForIdleSync()

        assertEquals(listOf(expectedResult), resultSpy.getRecorderResults())
    }

    @Test
    fun givenADialogFragmentListeningForAStringResult_whenNavigatingToANewDialogFragmentWhichFinishesWithResult_thenVerifyRecordedResultsIsEqualsToExpectedResult() {
        val resultSpy = TestResultSpy()
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.navigator.navigate(ListenForStringResultTestDialogFragmentRoute)
        waitForIdleSync()
        val expectedResult = "expected-result"

        activity.getSpecificFragment<ListenForStringResultTestDialogFragment>()
            .navigateToDialogFragmentReturningResult(expectedResult)
        waitForIdleSync()
        activity.getSpecificFragment<ReturnStringValueTestDialogFragment>().returnResult()
        waitForIdleSync()

        assertEquals(listOf(expectedResult), resultSpy.getRecorderResults())
    }

    @Test
    fun givenAFragmentListeningForAStringResult_whenNavigatingToANewDialogFragmentWhichFinishesWithDifferentResultType_thenVerifyRecordedResultsIsEmpty() {
        val resultSpy = TestResultSpy()
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.navigator.navigate(
            ListenForStringResulButReceiverWrongResultTypeFromDialogTestFragmentRoute
        )
        waitForIdleSync()

        activity.getSpecificFragment<ListenForStringResulButReceiverWrongResultTypeFromDialogTestFragment>()
            .navigateToDialogFragmentReturningWrongResultType()
        waitForIdleSync()
        activity.getSpecificFragment<ReturnIntValueTestDialogFragment>().returnResult()
        waitForIdleSync()

        assertEquals(emptyList<Any>(), resultSpy.getRecorderResults())
    }

    @Test
    fun givenASequenceOfDialogFragmentsListeningAndReturningAStringValue_whenLaunchingFirstDialogFragment_thenVerifyThatReturnedValuesAreCorrect() {
        val resultSpy = TestResultSpy()
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.navigator.navigate(ReturningValueSequence1TestDialogFragmentRoute)
        waitForIdleSync()

        activity.getSpecificFragment<ReturningValueSequence1TestDialogFragment>()
            .navigateToDialogFragment2()
        waitForIdleSync()
        activity.getSpecificFragment<ReturningValueSequenceOfDialogsTestFragment>()
            .navigateToDialogFragment3()
        waitForIdleSync()
        activity.getSpecificFragment<ReturningValueSequence3TestDialogFragment>()
            .returnResult()
        waitForIdleSync()

        assertEquals(
            listOf("arg_from_DialogFragment_3", "arg_from_DialogFragment_2"),
            resultSpy.getRecorderResults()
        )
    }

    @Test
    fun givenASequenceOfFragmentsListeningAndReturningAStringValue_whenLaunchingFirstDialogFragmentAndRecreatingActivity_thenVerifyThatReturnedValuesAreCorrect() {
        val resultSpy = TestResultSpy()
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.navigator.navigate(ReturningValueSequence1TestDialogFragmentRoute)
        waitForIdleSync()

        activity.getSpecificFragment<ReturningValueSequence1TestDialogFragment>()
            .navigateToDialogFragment2()
        waitForIdleSync()
        activity.getSpecificFragment<ReturningValueSequenceOfDialogsTestFragment>()
            .navigateToDialogFragment3()
        activity.recreateAndWait()
        ActivityStackContainer.peek()
            .getSpecificFragment<ReturningValueSequence3TestDialogFragment>()
            .returnResult()
        waitForIdleSync()

        assertEquals(
            listOf("arg_from_DialogFragment_3", "arg_from_DialogFragment_2"),
            resultSpy.getRecorderResults()
        )
    }

    @Test
    fun whenNavigatingToTwoNewRoutesAndClosingTheLastOne_thenCurrentTopDialogFragmentShouldBeTheFirstOne() {
        val activity = launchActivity<ContainerTestActivity>()
        activity.navigator.navigate(TestDialogFragmentRoute)

        activity.navigator.navigate(TestDialogFragment2Route)
        waitForIdleSync()
        activity.getSpecificFragment<TestDialogFragment2>().navigator.close()
        waitForIdleSync()

        assertEquals(TestDialogFragment::class, activity.getFragment()::class)
        assertEquals(1, activity.supportFragmentManager.fragments.size)
    }

    private inline fun <reified T : FragmentActivity> getActivity(): T =
        ActivityStackContainer.getByName(T::class.simpleName.toString()) as T

    data class FakeDialogFragmentRouteWithoutArgs(
        override val clazz: KClass<*> = TestDialogFragment::class
    ) : GeneratedDialogFragmentRoute

    data class FakeDialogFragmentRouteWithArgs(
        override val clazz: KClass<*> = TestDialogFragment::class,
        val arg1: String,
        val arg2: Double
    ) : GeneratedDialogFragmentRoute
}