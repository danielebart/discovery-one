package com.discoveryone

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.discoveryone.annotations.InternalDestinationArgumentMarker
import com.discoveryone.destinations.FragmentDestination
import com.discoveryone.extensions.scene
import com.discoveryone.initialization.ActivityStackContainer
import com.discoveryone.navigation.result.ActionLauncher
import com.discoveryone.testutils.ContainerTestActivity
import com.discoveryone.testutils.ListenForStringResultTestButReceiverWrongResultTypFragment
import com.discoveryone.testutils.ListenForStringResultTestButReceiverWrongResultTypFragmentDestination
import com.discoveryone.testutils.ListenForStringResultTestFragment
import com.discoveryone.testutils.ListenForStringResultTestFragmentDestination
import com.discoveryone.testutils.ReturnIntValueTestFragment
import com.discoveryone.testutils.ReturnStringValueTestFragment
import com.discoveryone.testutils.ReturningValueSequence1TestFragment
import com.discoveryone.testutils.ReturningValueSequence1TestFragmentDestination
import com.discoveryone.testutils.ReturningValueSequence2TestFragment
import com.discoveryone.testutils.ReturningValueSequence3TestFragment
import com.discoveryone.testutils.TestFragment
import com.discoveryone.testutils.TestResultSpy
import com.discoveryone.testutils.launchActivity
import com.discoveryone.testutils.recreateAndWait
import com.discoveryone.testutils.waitForIdleSync
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.reflect.KClass

class FragmentNavigationTest {

    @Test
    fun givenAFragmentDestinationWithoutArgs_whenNavigatingToThatFragment_thenCurrentTopFragmentShouldBeThatFragmentWithNoArgs() {
        val activity = launchActivity<ContainerTestActivity>()

        activity.scene.navigate(FakeFragmentDestinationWithoutArgs())
        waitForIdleSync()

        assertEquals(TestFragment::class, activity.getFragment()::class)
        assertEquals(0, activity.getFragment().arguments!!.size())
    }

    @Test
    fun givenAFragmentDestinationWithStringAndDoubleArgs_whenNavigatingToThatFragment_thenCurrentTopFragmentShouldBeThatFragmentWithThoseArgs() {
        val fakeFragmentDestination = FakeFragmentDestinationWithArgs(
            arg1 = "arg1_value",
            arg2 = 56789.0
        )
        val activity = launchActivity<ContainerTestActivity>()

        activity.scene.navigate(fakeFragmentDestination)
        waitForIdleSync()

        val currentFragmentArgs = activity.getFragment().arguments!!
        assertEquals(TestFragment::class, activity.getFragment()::class)
        assertEquals("arg1_value", currentFragmentArgs.getString("arg1"))
        assertEquals(56789.0, currentFragmentArgs.getDouble("arg2"), 0.0)
    }

    @Test
    fun givenAFragmentListeningForAStringResult_whenNavigatingToANewFragmentWhichFinishesWithResult_thenVerifyRecordedResultsIsEqualsToExpectedResult() {
        val resultSpy = TestResultSpy()
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.scene.navigate(ListenForStringResultTestFragmentDestination)
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
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.scene.navigate(
            ListenForStringResultTestButReceiverWrongResultTypFragmentDestination
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
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.scene.navigate(ReturningValueSequence1TestFragmentDestination)
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
    fun givenASequenceOfFragmentsListeningAndReturningAStringValue_whenLaunchingFirstFragmentAndRecreatingActivity_thenVerifyThatReturnedValuesAreCorrect() {
        val resultSpy = TestResultSpy()
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.scene.navigate(ReturningValueSequence1TestFragmentDestination)
        waitForIdleSync()

        activity.getSpecificFragment<ReturningValueSequence1TestFragment>()
            .navigateToFragment2()
        waitForIdleSync()
        activity.getSpecificFragment<ReturningValueSequence2TestFragment>()
            .navigateToFragment3()
        activity.recreateAndWait()
        ActivityStackContainer.peek().getSpecificFragment<ReturningValueSequence3TestFragment>()
            .returnResult()
        waitForIdleSync()

        assertEquals(
            listOf("arg_from_fragment_3", "arg_from_fragment_2"),
            resultSpy.getRecorderResults()
        )
    }

    private fun FragmentActivity.getFragment(position: Int = 0): Fragment =
        supportFragmentManager.fragments[position]

    private inline fun <reified F : Fragment> FragmentActivity.getSpecificFragment(): F =
        supportFragmentManager.fragments.first { it is F } as F

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
}