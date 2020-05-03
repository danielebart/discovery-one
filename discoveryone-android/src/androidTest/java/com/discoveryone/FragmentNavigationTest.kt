package com.discoveryone

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.discoveryone.annotations.InternalDestinationArgumentMarker
import com.discoveryone.destinations.FragmentDestination
import com.discoveryone.extensions.scene
import com.discoveryone.testutils.ContainerTestActivity
import com.discoveryone.testutils.TestFragment
import com.discoveryone.testutils.launchActivity
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

//    @Test
//    fun givenAFragmentListeningForAStringResult_whenNavigatingToANewFragmentWhichFinishesWithResult_thenVerifyRecordedResultsIsEqualsToExpectedResult() {
//        val resultSpy = TestResultSpy()
//        ActionLauncher.injectActivityResultSpy(resultSpy)
//        val activity = launchActivity<ContainerTestActivity>()
//        activity.scene.navigate(ListenForStringResultTestFragmentDestination)
//        waitForIdleSync()
//        val expectedResult = "expected-result"
//
//        activity.getSpecificFragment<ListenForStringResultTestFragment>()
//            .navigateToFragmentReturningResult(expectedResult)
//        waitForIdleSync()
//        activity.getSpecificFragment<ReturnStringValueTestFragment>().returnResult()
//        waitForIdleSync()
//
//        assertEquals(listOf(expectedResult), resultSpy.getRecorderResults())
//    }

    private fun FragmentActivity.getFragment(position: Int = 0): Fragment =
        supportFragmentManager.fragments[position]

    private inline fun <reified F : Fragment> FragmentActivity.getSpecificFragment(position: Int = 0): F =
        supportFragmentManager.fragments[position] as F

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