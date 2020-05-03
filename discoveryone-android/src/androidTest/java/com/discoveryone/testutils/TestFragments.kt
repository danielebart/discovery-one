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
class ListenForStringResultTestFragment : Fragment() {

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