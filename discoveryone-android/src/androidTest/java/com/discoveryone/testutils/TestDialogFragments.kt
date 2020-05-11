package com.discoveryone.testutils

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.discoveryone.annotations.ActivityRoute
import com.discoveryone.annotations.DialogFragmentRoute
import com.discoveryone.annotations.FragmentRoute
import com.discoveryone.annotations.RouteArgument
import com.discoveryone.extensions.navigator
import com.discoveryone.extensions.onResult

@DialogFragmentRoute
class TestDialogFragment : DialogFragment(com.discoveryone.test.R.layout.empty_layout)

@DialogFragmentRoute
class TestDialogFragment2 : DialogFragment(com.discoveryone.test.R.layout.empty_layout)

@DialogFragmentRoute
class ListenForStringResultTestDialogFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigator.onResult<String>("key_result") {}
    }

    fun navigateToDialogFragmentReturningResult(valueWhichNextDialogFragmentShouldReturn: String) {
        navigator.navigateForResult(
            "key_result",
            ReturnStringValueTestDialogFragmentRoute(valueWhichNextDialogFragmentShouldReturn)
        )
    }
}

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ListenForStringResultFromDialogTestFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigator.onResult<String>("key_result") { }
    }

    fun navigateToDialogFragmentReturningResult(valueWhichNextDialogFragmentShouldReturn: String) {
        navigator.navigateForResult(
            "key_result",
            ReturnStringValueTestDialogFragmentRoute(valueWhichNextDialogFragmentShouldReturn)
        )
    }
}

@DialogFragmentRoute(
    arguments = [RouteArgument("expectedReturningValue", String::class)]
)
class ReturnStringValueTestDialogFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    fun returnResult() {
        val expectedReturningValue = requireArguments().getString("expectedReturningValue")
        navigator.closeWithResult(expectedReturningValue)
    }
}

//////////////////////////////////////////////////////////////////

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ListenForStringResulButReceiverWrongResultTypeFromDialogTestFragment :
    Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigator.onResult<String>("key_result") {}
    }

    fun navigateToDialogFragmentReturningWrongResultType() {
        navigator.navigateForResult("key_result", ReturnIntValueTestDialogFragmentRoute)
    }
}

@DialogFragmentRoute
class ReturnIntValueTestDialogFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    fun returnResult() {
        navigator.closeWithResult(1231)
    }
}

//////////////////////////////////////////////////////////////////
@DialogFragmentRoute
class ReturningValueSequence1TestDialogFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigator.onResult<String>("key_result") {}
    }

    fun navigateToDialogFragment2() {
        navigator.navigateForResult("key_result", ReturningValueSequenceOfDialogsTestFragmentRoute)
        waitForIdleSync()
    }
}

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ReturningValueSequenceOfDialogsTestFragment :
    Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigator.onResult<String>("key_result2") {
            navigator.closeWithResult("arg_from_DialogFragment_2")
        }
    }

    fun navigateToDialogFragment3() {
        navigator.navigateForResult("key_result2", ReturningValueSequence3TestDialogFragmentRoute)
        waitForIdleSync()
    }
}

@DialogFragmentRoute
class ReturningValueSequence3TestDialogFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    fun returnResult() {
        navigator.closeWithResult("arg_from_DialogFragment_3")
    }
}
//////////////////////////////////////////////////////////////////

@ActivityRoute
class ListenForStringResultFromDialogTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.onResult<String>("key_result") {}
    }

    fun navigateToDialogFragmentReturningResult(valueWhichNextActivityShouldReturn: String) {
        navigator.navigateForResult(
            "key_result",
            ReturnStringValueTestDialogFragmentRoute(valueWhichNextActivityShouldReturn)
        )
    }
}