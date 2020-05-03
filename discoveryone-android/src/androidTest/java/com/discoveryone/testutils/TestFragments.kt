package com.discoveryone.testutils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.discoveryone.annotations.DestinationArgument
import com.discoveryone.annotations.FragmentNavigationDestination
import com.discoveryone.extensions.onResult
import com.discoveryone.extensions.scene

class TestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout)

@FragmentNavigationDestination(containerId = com.discoveryone.test.R.id.container)
class ListenForStringResultTestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scene.onResult<String>("key_result") {}
    }

    fun navigateToFragmentReturningResult(valueWhichNextFragmentShouldReturn: String) {
        scene.navigateForResult(
            "key_result",
            ReturnStringValueTestFragmentDestination(valueWhichNextFragmentShouldReturn)
        )
    }
}

@FragmentNavigationDestination(
    containerId = com.discoveryone.test.R.id.container,
    arguments = [DestinationArgument("expectedReturningValue", String::class)]
)
class ReturnStringValueTestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout) {

    fun returnResult() {
        val expectedReturningValue = requireArguments().getString("expectedReturningValue")
        scene.close(expectedReturningValue)
    }
}

//////////////////////////////////////////////////////////////////

@FragmentNavigationDestination(containerId = com.discoveryone.test.R.id.container)
class ListenForStringResultTestButReceiverWrongResultTypFragment :
    Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scene.onResult<String>("key_result") {}
    }

    fun navigateToFragmentReturningWrongResultType() {
        scene.navigateForResult("key_result", ReturnIntValueTestFragmentDestination)
    }
}

@FragmentNavigationDestination(containerId = com.discoveryone.test.R.id.container)
class ReturnIntValueTestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout) {

    fun returnResult() {
        scene.close(1231)
    }
}

//////////////////////////////////////////////////////////////////
@FragmentNavigationDestination(containerId = com.discoveryone.test.R.id.container)
class ReturningValueSequence1TestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scene.onResult<String>("key_result") {}
    }

    fun navigateToFragment2() {
        scene.navigateForResult("key_result", ReturningValueSequence2TestFragmentDestination)
        waitForIdleSync()
    }
}

@FragmentNavigationDestination(containerId = com.discoveryone.test.R.id.container)
class ReturningValueSequence2TestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scene.onResult<String>("key_result2") {
            scene.close("arg_from_fragment_2")
        }
    }

    fun navigateToFragment3() {
        scene.navigateForResult("key_result2", ReturningValueSequence3TestFragmentDestination)
        waitForIdleSync()
    }
}

@FragmentNavigationDestination(containerId = com.discoveryone.test.R.id.container)
class ReturningValueSequence3TestFragment : Fragment(com.discoveryone.test.R.layout.empty_layout) {

    fun returnResult() {
        scene.close("arg_from_fragment_3")
    }
}