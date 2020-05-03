package com.discoveryone

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.discoveryone.annotations.InternalRouteArgumentMarker
import com.discoveryone.extensions.close
import com.discoveryone.extensions.scene
import com.discoveryone.initialization.ActivityStackContainer
import com.discoveryone.navigation.result.ActionLauncher
import com.discoveryone.routes.GeneratedFragmentRoute
import com.discoveryone.testutils.ContainerTestActivity
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
import com.discoveryone.testutils.launchActivity
import com.discoveryone.testutils.recreateAndWait
import com.discoveryone.testutils.waitForIdleSync
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.reflect.KClass

class FragmentNavigationTest {

    @Test
    fun givenAFragmentRouteWithoutArgs_whenNavigatingToThatFragment_thenCurrentTopFragmentShouldBeThatFragmentWithNoArgs() {
        val activity = launchActivity<ContainerTestActivity>()

        activity.scene.navigate(FakeFragmentRouteWithoutArgs())
        waitForIdleSync()

        assertEquals(TestFragment::class, activity.getFragment()::class)
        assertEquals(0, activity.getFragment().arguments!!.size())
    }

    @Test
    fun givenAFragmentRouteWithStringAndDoubleArgs_whenNavigatingToThatFragment_thenCurrentTopFragmentShouldBeThatFragmentWithThoseArgs() {
        val fakeFragmentRoute = FakeFragmentRouteWithArgs(
            arg1 = "arg1_value",
            arg2 = 56789.0
        )
        val activity = launchActivity<ContainerTestActivity>()

        activity.scene.navigate(fakeFragmentRoute)
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
        activity.scene.navigate(ListenForStringResultTestFragmentRoute)
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
        ActionLauncher.injectActivityResultSpy(resultSpy)
        val activity = launchActivity<ContainerTestActivity>()
        activity.scene.navigate(ReturningValueSequence1TestFragmentRoute)
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
        activity.scene.navigate(ReturningValueSequence1TestFragmentRoute)
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

    @Test
    fun whenNavigatingToTwoNewRoutesAndClosingTheLastOne_thenCurrentTopFragmentShouldBeTheFirstOne() {
        val activity = launchActivity<ContainerTestActivity>()
        activity.scene.navigate(TestFragmentRoute)

        activity.scene.navigate(TestFragment2Route)
        waitForIdleSync()
        activity.getSpecificFragment<TestFragment2>().scene.close()
        waitForIdleSync()

        assertEquals(TestFragment::class, activity.getFragment()::class)
        assertEquals(1, activity.supportFragmentManager.fragments.size)
    }

    private fun FragmentActivity.getFragment(position: Int = 0): Fragment =
        supportFragmentManager.fragments[position]

    private inline fun <reified F : Fragment> FragmentActivity.getSpecificFragment(): F =
        supportFragmentManager.fragments.first { it is F } as F

    data class FakeFragmentRouteWithoutArgs(
        override val clazz: KClass<*> = TestFragment::class,
        override val containerId: Int = com.discoveryone.test.R.id.container
    ) : GeneratedFragmentRoute

    data class FakeFragmentRouteWithArgs(
        override val clazz: KClass<*> = TestFragment::class,
        override val containerId: Int = com.discoveryone.test.R.id.container,
        @InternalRouteArgumentMarker val arg1: String,
        @InternalRouteArgumentMarker val arg2: Double
    ) : GeneratedFragmentRoute
}