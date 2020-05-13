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
        navigator.onResult<String, ReturnStringValueTestDialogFragmentRoute> {}
    }

    fun navigateToDialogFragmentReturningResult(valueWhichNextDialogFragmentShouldReturn: String) {
        navigator.navigateForResult(
            ReturnStringValueTestDialogFragmentRoute(valueWhichNextDialogFragmentShouldReturn)
        )
    }
}

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ListenForStringResultFromDialogTestFragment :
    DialogFragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigator.onResult<String, ReturnStringValueTestDialogFragmentRoute> { }
    }

    fun navigateToDialogFragmentReturningResult(valueWhichNextDialogFragmentShouldReturn: String) {
        navigator.navigateForResult(
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
        navigator.onResult<String, ReturnIntValueTestDialogFragmentRoute> {}
    }

    fun navigateToDialogFragmentReturningWrongResultType() {
        navigator.navigateForResult(ReturnIntValueTestDialogFragmentRoute)
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
        navigator.onResult<String, ReturningValueSequenceOfDialogsTestFragmentRoute> {}
    }

    fun navigateToDialogFragment2() {
        navigator.navigateForResult(ReturningValueSequenceOfDialogsTestFragmentRoute)
        waitForIdleSync()
    }
}

@FragmentRoute(containerId = com.discoveryone.test.R.id.container)
class ReturningValueSequenceOfDialogsTestFragment :
    Fragment(com.discoveryone.test.R.layout.empty_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigator.onResult<String, ReturningValueSequence3TestDialogFragmentRoute> {
            navigator.closeWithResult("arg_from_DialogFragment_2")
        }
    }

    fun navigateToDialogFragment3() {
        navigator.navigateForResult(ReturningValueSequence3TestDialogFragmentRoute)
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
        navigator.onResult<String, ReturnStringValueTestDialogFragmentRoute> {}
    }

    fun navigateToDialogFragmentReturningResult(valueWhichNextActivityShouldReturn: String) {
        navigator.navigateForResult(
            ReturnStringValueTestDialogFragmentRoute(valueWhichNextActivityShouldReturn)
        )
    }
}