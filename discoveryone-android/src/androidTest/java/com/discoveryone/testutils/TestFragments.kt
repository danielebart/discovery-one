package com.discoveryone.testutils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.discoveryone.annotations.FragmentRoute
import com.discoveryone.annotations.RouteArgument
import com.discoveryone.extensions.navigator
import com.discoveryone.extensions.onResult

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class TestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout)

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class TestFragment2 : Fragment(com.discoveryone.test.R.layout.empty_layout)

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ListenForStringResultTestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigator.onResult<String>("key_result") {}
    }

    fun navigateToFragmentReturningResult(valueWhichNextFragmentShouldReturn: String) {
        navigator.navigateForResult(
            "key_result",
            ReturnStringValueTestFragmentRoute(valueWhichNextFragmentShouldReturn)
        )
    }
}

@FragmentRoute(
    containerId = com.discoveryone.test.R.id.container,
    arguments = [RouteArgument("expectedReturningValue", String::class)]
)
class ReturnStringValueTestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout) {

    fun returnResult() {
        val expectedReturningValue = requireArguments().getString("expectedReturningValue")
        navigator.closeWithResult(expectedReturningValue)
    }
}

//////////////////////////////////////////////////////////////////

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ListenForStringResultTestButReceiverWrongResultTypFragment :
    Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigator.onResult<String>("key_result") {}
    }

    fun navigateToFragmentReturningWrongResultType() {
        navigator.navigateForResult("key_result", ReturnIntValueTestFragmentRoute)
    }
}

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ReturnIntValueTestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout) {

    fun returnResult() {
        navigator.closeWithResult(1231)
    }
}

//////////////////////////////////////////////////////////////////
@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ReturningValueSequence1TestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigator.onResult<String>("key_result") {}
    }

    fun navigateToFragment2() {
        navigator.navigateForResult("key_result", ReturningValueSequence2TestFragmentRoute)
        waitForIdleSync()
    }
}

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ReturningValueSequence2TestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigator.onResult<String>("key_result2") {
            navigator.closeWithResult("arg_from_fragment_2")
        }
    }

    fun navigateToFragment3() {
        navigator.navigateForResult("key_result2", ReturningValueSequence3TestFragmentRoute)
        waitForIdleSync()
    }
}

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ReturningValueSequence3TestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout) {

    fun returnResult() {
        navigator.closeWithResult("arg_from_fragment_3")
    }
}